import pandas as pd
from pandas import DataFrame

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

def parse_and_check_limits(raw_df:DataFrame)->DataFrame:
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