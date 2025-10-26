import os
import pandas as pd
import sqlite3
import seaborn as sns
import matplotlib.pyplot as plt

def read_all_files(folder:str)->list[pd.DataFrame]:
    sqlite_files: list[str] = [f for f in os.listdir(folder) if f.endswith((".sqlite", ".db"))]
    dfs: list[pd.DataFrame] = []
    for file in sqlite_files:
        file_path: str = os.path.join(folder, file)
        conn: sqlite3.Connection = sqlite3.connect(file_path)

        try:
            df: pd.DataFrame = pd.read_sql_query(f"SELECT * FROM logs", conn)
            print(df.head())
            df["source_file"] = file  # optional: track origin
            print(f"Loaded {len(df)} rows from {file}")
        except Exception as e:
            print(f"Error reading logs from {file}: {e}")
        finally:
            conn.close()
    return dfs

processed_files: list[pd.DataFrame] = read_all_files("/Users/roger/Documents/tmp/rosana/tmp/01/")

for file in processed_files:
    print(f"Processing {file['source_file']}")

df_all: pd.DataFrame = pd.concat(processed_files, ignore_index=True)  # keeps all rows
print(f"Combined DataFrame. Total size is {df_all.size}")


def plot_dut(df, dut_id):
    df_dut = df_all[df_all['dut_id'] == dut_id]

    plt.figure(figsize=(12, 6))
    sns.lineplot(data=df_dut, x='timestamp', y='value', hue='variable', marker='o')
    plt.title(f"Variables over time for DUT: {dut_id}")
    plt.xlabel("Time")
    plt.ylabel("Value")
    plt.xticks(rotation=45)
    plt.legend(title='Variable')
    plt.tight_layout()
    plt.show()


# Example usage
plot_dut(df_all, "TLMA-18718")