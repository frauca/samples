import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

# Load CSV
df = pd.read_csv("simple.csv")
print(df.head())

# First plot: Score by Student
df_sorted = df.sort_values(by="Score")
sns.lineplot(x="Student", y="Score", data=df_sorted, marker="o")
plt.title("Exam Score by Student (sorted)")
plt.xticks(rotation=45)
plt.show()   # <-- shows first plot

# Second plot: Hours vs Score
df_sorted = df.sort_values(by="Hours_Studied")
sns.lineplot(x="Hours_Studied", y="Score", data=df_sorted, marker="o")
plt.title("Hours Studied vs Score")
plt.show()   # <-- shows second plot

fig, axes = plt.subplots(1, 2, figsize=(12, 5))  # 1 row, 2 columns

# Plot 1: Score by Student
df_sorted = df.sort_values(by="Score")
sns.lineplot(x="Student", y="Score", data=df_sorted, marker="o", ax=axes[0])
axes[0].set_title("Exam Score by Student (sorted)")
axes[0].tick_params(axis='x', rotation=45)

# Plot 2: Hours vs Score
df_sorted = df.sort_values(by="Hours_Studied")
sns.lineplot(x="Hours_Studied", y="Score", data=df_sorted, marker="o", ax=axes[1])
axes[1].set_title("Hours Studied vs Score")

plt.tight_layout()
plt.show()

# Min-Max normalize each column we want to compare
df_normalized = df.copy()
for col in ["Hours_Studied", "Score"]:
    df_normalized[col] = (df[col] - df[col].min()) / (df[col].max() - df[col].min())

df_normalized.sort_values("Hours_Studied", inplace=True)
# Melt for seaborn
df_melted = df_normalized.melt(id_vars=["Student"], value_vars=["Hours_Studied", "Score"],
                               var_name="Metric", value_name="Value")

# Plot
sns.lineplot(x="Student", y="Value", hue="Metric", data=df_melted, marker="o")
plt.title("Normalized Study Hours vs Scores by Student")
plt.xticks(rotation=45)
plt.ylabel("Normalized Value (0-1)")
plt.show()