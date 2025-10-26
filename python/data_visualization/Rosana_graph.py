import sqlite3
from pathlib import Path
from tkinter.filedialog import askdirectory

import pandas as pd
from pandas import DataFrame
import plotly.express as px
import re

diagnostics_limits = {
    "current": {"MIN": 0.7, "MAX": 2, "unit": "A"},
    "touch_screen": {"MIN": 0, "MAX": 0, "unit": "bool"},
    "cpu_usage": {"MIN": 75, "MAX": 100, "unit": "%"},
    "light_sensor": {"MIN": 0, "MAX": 200, "unit": "lm"},
    "fan_speed": {"MIN": 2000, "MAX": 9999, "unit": "RPM"},
    "fan_ground_voltage": {"MIN": 0, "MAX": 600, "unit": "V"},
    "fan_vcc_voltage": {"MIN": 6600, "MAX": 7400, "unit": "V"},
    "emmc_memory": {"MIN": 1, "MAX": 1, "unit": "bool"},
    "ram_memory": {"MIN": 1, "MAX": 1, "unit": "bool"},
    "rtt": {"MIN": 0, "MAX": 15, "unit": "ms"},
    "sqi": {"MIN": 3, "MAX": 7, "unit": "(signal quality)"},
    "mse": {"MIN": 0, "MAX": 0, "unit": "(mean squared error)"},
    "rx_bitrate": {"MIN": 10, "MAX": 100, "unit": "Mbps"},
    "tx_bitrate": {"MIN": 10, "MAX": 100, "unit": "Mbps"},
    "pc_to_box_ldp": {"MIN": 0, "MAX": 10, "unit": "%"},
    "box_to_pc_ldp": {"MIN": 0, "MAX": 10, "unit": "%"},
    "net_usage_tx": {"MIN": 10, "MAX": 100, "unit": "Mbps"},
    "net_usage_rx": {"MIN": 10, "MAX": 100, "unit": "Mbps"},
    "fps": {"MIN": 60, "MAX": 60, "unit": "FPS"},
    "LDO1_PMIC1": {"MIN": 1615, "MAX": 1785, "unit": "mV"},
    "LDO2_PMIC1": {"MIN": 1710, "MAX": 1890, "unit": "mV"},
    "SW1_FB_PMIC1": {"MIN": 950, "MAX": 1050, "unit": "mV"},
    "SW3_FB_PMIC1": {"MIN": 1080.15, "MAX": 1193.85, "unit": "mV"},
    "MEAS_5V": {"MIN": 4750, "MAX": 5250, "unit": "mV"},
    "KL30_P_MEAS": {"MIN": 12150, "MAX": 14850, "unit": "mV"},
    "TEMP_IC_PMIC1": {"MIN": -15, "MAX": 100, "unit": "ºC"},
    "TEMP_IC_PMIC2": {"MIN": -15, "MAX": 100, "unit": "ºC"},
    "DISP_MEAS": {"MIN": 7600, "MAX": 8400, "unit": "mV"},
    "3V3_EXT_MEAS": {"MIN": 3135, "MAX": 3465, "unit": "mV"},
    "TEMP_NTC": {"MIN": -15, "MAX": 100, "unit": "ºC"},
    "LCD_NTC": {"MIN": -15, "MAX": 100, "unit": "ºC"},
}

def print_removed_variables(removed_df:DataFrame)->None:
    removed_df["value_numeric"] = pd.to_numeric(removed_df["value"], errors="coerce")
    stats = (
        removed_df.groupby("variable")["value_numeric"]
        .agg(["min", "max"])
        .reset_index()
    )
    for _, row in stats.iterrows():
        print(f"Removed - {row['variable']}: min={row['min']}, max={row['max']}")

def get_db_files(folder_path:Path)->list[Path]:
    """Recursively get all .db files in the given folder path."""
    return [p for p in folder_path.rglob('*.db') if p.is_file()]

def parse_and_check_limits(raw_df):
    """Check each variable against its diagnostic limits."""
    raw_df["timestamp"] = pd.to_datetime(raw_df["timestamp"])
    filtered_df = raw_df[raw_df["variable"].isin(diagnostics_limits.keys())].copy()
    removed_df = raw_df[~raw_df["variable"].isin(diagnostics_limits.keys())].copy()
    print_removed_variables(removed_df)

    def check_limits(value, MIN, MAX, unit):
        if MIN is None:
            return "N/A"
        return "PASS" if MIN <= value <= MAX else "FAIL"

    filtered_df["results"] = filtered_df.apply(
        lambda row: check_limits(
            row["value"],
            **diagnostics_limits.get(
                row["variable"], {"MIN": None, "MAX": None, "unit": None}
            ),
        ),
        axis=1,
    )
    return filtered_df

def read_db_file(file:Path)->DataFrame:
    """Read a single .db file and return its contents as a DataFrame."""
    try:
        conn = sqlite3.connect(file)
        df = pd.read_sql_query("SELECT * FROM logs", conn)
    finally:
        conn.close()
    return df



def homogeneize_dut_id(df: DataFrame) -> DataFrame:
    """Homogeniza la columna `dut_id`. Extrae la primera secuencia de dígitos
    de cada valor y la usa como identificador uniforme (por ejemplo
    TLMA-18718 -> 18718). Devuelve un nuevo DataFrame sin modificar el original."""


    def _normalize(val):
        s = str(val).strip()
        m = re.search(r"(\d+)", s)
        return m.group(1) if m else s

    df["dut_id"] = df["dut_id"].apply(_normalize)
    return df


def ordenar_by_timestamp(df:DataFrame)->DataFrame:
    return df.sort_values(by="timestamp").reset_index(drop=True)

def read_db_files(files:list[Path])->DataFrame:
    """Read multiple .db files and combine them into a single DataFrame."""
    all_df:list[DataFrame] = []
    for file in files:

        print(f"Reading: {file}")
        df = read_db_file(file)
        print(f"o - rows: {len(df)}")
        if len(df) > 0:
            filtered_df = parse_and_check_limits(df)
            homogeneized = homogeneize_dut_id(filtered_df)
            all_df.append(homogeneized)

    db_cocatenated = pd.concat(all_df, ignore_index=True)
    ordered_df = ordenar_by_timestamp(db_cocatenated)
    print(f"Total rows after processing: {len(ordered_df)}")
    return ordered_df

def separate_by_dut_id(df:DataFrame)->dict[str,DataFrame]:
    dut_dict:dict[str,DataFrame] = {}
    unique_dut_ids = df['dut_id'].unique()
    for dut_id in unique_dut_ids:
        dut_dict[dut_id] = df[df['dut_id'] == dut_id]
    return dut_dict

def paint_files(output_dir:Path, dut_dict:dict[str,DataFrame], print_excel:bool)->None:

    for dut_id, diag_df in dut_dict.items():
        dut_output_dir = output_dir / "processed" / dut_id
        dut_output_dir.mkdir(parents=True, exist_ok=True)

        # 1️⃣ Full diagnostics plot
        print(f"Full diagnostic plot of: {dut_id}")
        fig = px.line(diag_df, x="timestamp", y="value", color="variable", title=f"Diagnostics - {dut_id}", markers=True)
        fig.write_html(dut_output_dir / f"{dut_id}_diag_plot.html")

        # 2️⃣ Fail diagnostics plot
        print(f"Failured plot of: {dut_id}")
        diag_df_fail = diag_df[diag_df["results"] == "FAIL"]
        fig = px.line(diag_df_fail, x="timestamp", y="value", color="variable", title=f"Diagnostics Fails - {dut_id}", markers=True)
        fig.write_html(dut_output_dir / f"{dut_id}_diag_fails_plot.html")

        # 3️⃣ Excel of fails
        if print_excel:
            print(f"Excel of fails of: {dut_id}")
            diag_df_fail.to_excel(dut_output_dir / f"{dut_id}_diagnostics_fails.xlsx", index=False)

        # 2️⃣ Positive diagnostics plot
        print(f"Positive plot of: {dut_id}")
        diag_df_positive = diag_df[diag_df["value"] >=  0]
        fig = px.line(diag_df_positive, x="timestamp", y="value", color="variable", title=f"Diagnostics Positive - {dut_id}", markers=True)
        fig.write_html(dut_output_dir / f"{dut_id}_diag_positive_plot.html")


def main():
    folder_path = Path(askdirectory(title="Select directory containing .db files"))
    db_files = get_db_files(folder_path)
    db_all = read_db_files(db_files)
    dut_dict = separate_by_dut_id(db_all)
    paint_files(folder_path, dut_dict, print_excel=False)

    print("done")


if __name__ == "__main__":
    main()