import pandas as pd
import sqlite3
import seaborn as sns
import matplotlib.pyplot as plt
import numpy as np

conn: sqlite3.Connection = sqlite3.connect("/Users/roger/Documents/tmp/rosana/tmp/01/log_2025_08_26_14_24_22.6.db")

df: pd.DataFrame = pd.read_sql_query(f"SELECT * FROM logs", conn)
print(df.head())
print(df.size)

conn.close()

unique_dut_ids = df['dut_id'].unique()

# Print them
print(unique_dut_ids)

counts = df['dut_id'].value_counts()
print(counts)


def sample_timestamps_per_dut(df, n=10):
    sampled = df.groupby('dut_id').apply(
        lambda x: x.drop_duplicates(subset='timestamp').head(n)
    ).reset_index(drop=True)
    return sampled

# Example usage
df_sampled = sample_timestamps_per_dut(df, n=500)
print(df_sampled.head())

def plot_dut(df, dut_id, n_ticks=10):
    df_dut = df[df['dut_id'] == dut_id]

    plt.figure(figsize=(12, 6))
    sns.lineplot(data=df_dut, x='timestamp', y='value', hue='variable', marker='o')
    plt.title(f"Variables over time for DUT: {dut_id}")
    plt.xlabel("Time")
    plt.ylabel("Value")

    # Rotate x labels
    plt.xticks(rotation=45)

    # Show only n_ticks evenly spaced x labels
    timestamps = df_dut['timestamp'].drop_duplicates()
    if len(timestamps) > n_ticks:
        tick_indices = np.linspace(0, len(timestamps)-1, n_ticks, dtype=int)
        plt.xticks(ticks=tick_indices, labels=timestamps.iloc[tick_indices], rotation=45)

    plt.legend(title='Variable')
    plt.tight_layout()
    plt.show()

print("Start drawing")
#plot_dut(df, "TLMA-18718")

print(df_sampled[df_sampled["variable"]=="current"].head(n=10))