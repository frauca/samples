import os
import sqlite3
import sys

import pandas as pd
import plotly.express as px
from tqdm import tqdm

BASE_PATH = os.path.abspath(os.path.dirname(__file__))

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


def parse_db(db_file):
    """Parses a .db file and returns a DataFrame."""
    conn = sqlite3.connect(db_file)
    df = pd.read_sql_query("SELECT * FROM logs", conn)
    conn.close()
    return df


def parse_and_check_limits(raw_df):
    """Check each variable against its diagnostic limits."""
    raw_df["timestamp"] = pd.to_datetime(raw_df["timestamp"])
    filtered_df = raw_df[raw_df["variable"].isin(diagnostics_limits.keys())].copy()

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
def paint_files(output_dir,file_name,diag_df):
    # 1️⃣ Full diagnostics plot
    fig = px.line(diag_df, x = "timestamp", y = "value", color = "variable", title = "Diagnostics", markers = True)
    fig.write_html(os.path.join(output_dir, f"{file_name}_diag_plot.html"))


    # 2️⃣ Fail diagnostics plot
    diag_df_fail = diag_df[diag_df["results"] == "FAIL"]
    fig = px.line(diag_df_fail, x="timestamp", y="value", color="variable", title="Diagnostics Fails", markers=True)
    fig.write_html(os.path.join(output_dir, f"{file_name}_diag_fails_plot.html"))


    # 3️⃣ Excel of fails
    diag_df_fail.to_excel(os.path.join(output_dir, f"{file_name}_diagnostics_fails.xlsx"), index=False)



def process_db_file(db_path):
    """Processes a single .db file: plot full graph, fail graph, and save Excel of fails."""
    file_name = os.path.splitext(os.path.basename(db_path))[0]

    df = parse_db(db_path)
    diag_df = parse_and_check_limits(df)
    unique_dut_ids = diag_df['dut_id'].unique()
    for dut_id in unique_dut_ids:
        df_dut = diag_df[diag_df['dut_id'] == dut_id]

        output_dir = os.path.join(os.path.dirname(db_path),dut_id, file_name)
        os.makedirs(output_dir, exist_ok=True)

        paint_files(output_dir, file_name,df_dut)


if __name__ == "__main__":
    from tkinter import Tk
    from tkinter.filedialog import askdirectory

    # Set CMD window title
    os.system("title IDDSH0 - DB Parser")

    # Show ASCII banner
    print("┌──────────────────────────────────────┐")
    print("│░░▒▒▓▓██[ IDDSH0 - DB Parser ]██▓▓▒▒░░│")
    print("└──────────────────────────────────────┘\n")

    Tk().withdraw()
    folder_path = askdirectory(title="Select directory containing .db files")

    if folder_path:
        # Collect all .db files recursively and calculate total steps (3 per .db)
        db_files = []
        for root, _, files in os.walk(folder_path):
            for file in files:
                if file.endswith(".db"):
                    db_files.append(os.path.join(root, file))

        total_steps = len(db_files)
        print(f"Found {len(db_files)} .db files ({total_steps} steps total).\n")

        # Redirect stdout temporarily to ignore external logs
        class DummyStream:
            def write(self, x): pass
            def flush(self): pass

        original_stdout = sys.stdout
        sys.stdout = DummyStream()

        try:
            # Real progress bar
            with tqdm(total=total_steps, desc="Processing", unit="step") as pbar:
                for db_path in db_files:
                    try:
                        process_db_file(db_path)
                    finally:
                        pbar.update(1)
        finally:
            sys.stdout = original_stdout  # Restore stdout
