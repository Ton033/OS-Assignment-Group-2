import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv('aggregated_test_results.csv')

df['NumberOfClients'] = pd.to_numeric(df['NumberOfClients'], errors='coerce')
df['AverageResponseTimeMs'] = pd.to_numeric(df['AverageResponseTimeMs'], errors='coerce')
df['MedianResponseTimeMs'] = pd.to_numeric(df['MedianResponseTimeMs'], errors='coerce')
df['95thPercentileResponseTimeMs'] = pd.to_numeric(df['95thPercentileResponseTimeMs'], errors='coerce')

df.dropna(inplace=True)

df.sort_values(by='NumberOfClients', inplace=True)

plt.figure(figsize=(10, 6))

markers = {
    'AverageResponseTimeMs': 'o-',
    'MedianResponseTimeMs': 's--',
    '95thPercentileResponseTimeMs': 'd-.'
}

for server_type in df['ServerType'].unique():
    subset = df[df['ServerType'] == server_type]
    for metric, marker in markers.items():
        plt.plot(subset['NumberOfClients'], subset[metric], marker, label=f'{server_type} {metric.split("ResponseTimeMs")[0]}')

plt.xlabel('Number of Clients')
plt.ylabel('Response Time (ms)')
plt.title('Server Performance Comparison')
plt.legend()
plt.grid(True)
plt.show()
