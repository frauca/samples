
import sqlite3

from pathlib import Path
from tkinter.filedialog import askdirectory, Directory

from pandas import DataFrame, read_sql_query, concat

from sanity import parse_and_check_limits
from print import paint_files


def get_all_db_files(base_folder: Path)->list[Path]:
    print("Getting all .db files")
    db_files:list[Path] = []
    for db_file in base_folder.glob("*.db"):
        db_files.append(db_file)
    return db_files

def read_db(db_file:Path)->DataFrame:
    conn: sqlite3.Connection = sqlite3.connect(db_file)
    try:
        print(f"Reading .db file from {db_file}")
        df: DataFrame = read_sql_query(f"SELECT * FROM logs", conn)
        df["source"] = db_file.stem
        print(f"Pre-Sanity {len(df)} rows")
        df = parse_and_check_limits(df)
        print(f"Pos-Sanity {len(df)} rows")
        return df
    finally:
        conn.close()

def get_full_database(db_files:list[Path]) -> DataFrame:
    dfs = [read_db(db_file) for db_file in db_files]
    return concat(dfs, ignore_index=True)

def paint_all(db_path:Path,diag_df:DataFrame) -> None:
    unique_dut_ids = diag_df['dut_id'].unique()
    for dut_id in unique_dut_ids:
        df_dut = diag_df[diag_df['dut_id'] == dut_id]

        paint_files(db_path, dut_id, df_dut)

def main()->None:
    base_folder = Path(askdirectory(title="Select directory containing .db files"))
    all_db_files = get_all_db_files(base_folder)
    df:DataFrame = get_full_database(all_db_files)
    print(f"Total Rows: {len(df)}")
    paint_all(base_folder, df)
    print("Done")

if __name__ == "__main__":
    main()