from pathlib import Path

import plotly.express as px
import os

from pandas import DataFrame


def paint_files(db_path:Path, file_name:str, df:DataFrame)->None:
    output_dir = os.path.join(db_path, file_name)
    os.makedirs(output_dir, exist_ok=True)

    # 1️⃣ Full diagnostics plot
    print(f"Plot {output_dir}")
    fig = px.line(df, x ="timestamp", y ="value", color ="variable", title ="Diagnostics", markers = True)
    fig.write_html(os.path.join(output_dir, f"{file_name}_diag_plot.html"))


    # 2️⃣ Fail diagnostics plot
    print(f"Fails {output_dir}")
    diag_df_fail = df[df["results"] == "FAIL"]
    fig = px.line(diag_df_fail, x="timestamp", y="value", color="variable", title="Diagnostics Fails", markers=True)
    fig.write_html(os.path.join(output_dir, f"{file_name}_diag_fails_plot.html"))
