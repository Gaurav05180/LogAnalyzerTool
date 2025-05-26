let countryChartInstance;
let cityChartInstance;
let osChartInstance;
let osVersionChartInstance;
let hourlyChartInstance;
let cityStatsData = [];
let osVersionStatsData = [];

// Utility: Generate unique random colors
function generateUniqueColors(count) {
  const baseColors = [
    '#3E8E7E', '#F2B134', '#E55D87', '#7A57D1', '#00A8E8',
    '#FF715B', '#36B37E', '#F28500', '#F76C6C', '#6A0572',
    '#FFB400', '#247BA0', '#70C1B3', '#D7263D', '#1B998B',
  ];

  const colors = [];

  // Add base designer colors first
  for (let i = 0; i < count && i < baseColors.length; i++) {
    colors.push(baseColors[i]);
  }

  // Generate extra unique colors using HSL if needed
  for (let i = baseColors.length; i < count; i++) {
    const hue = (i * 137.508) % 360; // Using golden angle for uniform hue distribution
    const saturation = 65 + (i % 10); // Slight variation in saturation
    const lightness = 55 + (i % 10); // Slight variation in lightness
    const hslColor = `hsl(${hue.toFixed(2)}, ${saturation}%, ${lightness}%)`;
    colors.push(hslToHex(hslColor));
  }

  return colors;
}

// Helper: Convert HSL to HEX
function hslToHex(hsl) {
  const [h, s, l] = hsl.match(/\d+(\.\d+)?/g).map(Number);
  const a = s / 100;
  const b = l / 100;

  const k = n => (n + h / 30) % 12;
  const f = n =>
    b - a * Math.min(b, 1 - b) * Math.max(-1, Math.min(k(n) - 3, Math.min(9 - k(n), 1)));

  const toHex = x =>
    Math.round(x * 255)
      .toString(16)
      .padStart(2, '0');

  return `#${toHex(f(0))}${toHex(f(8))}${toHex(f(4))}`;
}


// 1. Load city stats
fetch('http://localhost:8080/api/city-stats')
  .then(res => res.json())
  .then(data => cityStatsData = data);

// 2. Country Chart
fetch('http://localhost:8080/api/country-stats')
  .then(res => res.json())
  .then(data => {
    const labels = data.map(item => item.label);
    const counts = data.map(item => item.count);
    const total = counts.reduce((a, b) => a + b, 0);
    const backgroundColors = generateUniqueColors(labels.length);

    const ctx = document.getElementById('countryChart').getContext('2d');
    countryChartInstance = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels,
        datasets: [{
          label: 'Users',
          data: counts,
          backgroundColor: backgroundColors,
          borderWidth: 0,
          hoverOffset: 10
        }]
      },
      options: {
        onClick: (e, elements) => {
          if (elements.length > 0) {
            const countryIndex = elements[0].index;
            const selectedCountry = labels[countryIndex];
            renderCityChart(selectedCountry);
            document.getElementById('countryChart').parentElement.style.display = 'none';
            document.getElementById('cityChart').parentElement.style.display = 'block';
            document.getElementById('backButton').style.display = 'inline-block';
          }
        },
        cutout: '55%',
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              generateLabels: chart => chart.data.labels.map((label, i) => ({
                text: `${label} (${chart.data.datasets[0].data[i]})`,
                fillStyle: chart.data.datasets[0].backgroundColor[i],
                strokeStyle: chart.data.datasets[0].backgroundColor[i],
                index: i
              }))
            }
          },
          title: {
            display: true,
            text: 'Country-Wise User Access'
          },
          tooltip: {
            callbacks: {
              label: context => {
                const value = context.parsed;
                const percentage = ((value / total) * 100).toFixed(1);
                return `${context.label}: ${value} (${percentage}%)`;
              }
            }
          },
          datalabels: {
            color: '#000',
            font: { weight: 'bold', size: 12 },
            formatter: value => `${((value / total) * 100).toFixed(0)}%`
          }
        }
      },
      plugins: [ChartDataLabels]
    });
  });

// 3. Render City Chart
function renderCityChart(countryName) {
  const filteredData = cityStatsData.filter(d => d.country === countryName);
  const labels = filteredData.map(item => item.city);
  const counts = filteredData.map(item => item.count);

  const ctx = document.getElementById('cityChart').getContext('2d');
  if (cityChartInstance) cityChartInstance.destroy();

  cityChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels,
      datasets: [{
        label: `Accesses in ${countryName}`,
        data: counts,
        backgroundColor: '#42A5F5'
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: { display: false },
        title: {
          display: true,
          text: `City-wise Access in ${countryName}`
        }
      }
    }
  });
}

// 4. Back to Country View
document.getElementById('backButton').addEventListener('click', () => {
  document.getElementById('cityChart').parentElement.style.display = 'none';
  document.getElementById('countryChart').parentElement.style.display = 'block';
  document.getElementById('backButton').style.display = 'none';
});

// 5. Load OS Stats

fetch('http://localhost:8080/api/os-stats')
  .then(res => res.json())
  .then(data => {
    const rawLabels = data.map(item => item.label);
    const counts = data.map(item => item.count);
    const total = counts.reduce((a, b) => a + b, 0);
    const backgroundColors = generateUniqueColors(rawLabels.length);

    // Labels now include counts in brackets
    const labels = rawLabels.map((label, i) => `${label} (${counts[i]})`);

    const ctx = document.getElementById('osChart').getContext('2d');
    osChartInstance = new Chart(ctx, {
      type: 'pie',
      data: {
        labels,
        datasets: [{
          label: 'OS',
          data: counts,
          backgroundColor: backgroundColors
        }]
      },
      options: {
        onClick: (e, elements) => {
          if (elements.length > 0) {
            const index = elements[0].index;
            // Extract original label without count
            const selectedOS = rawLabels[index];
            renderOSVersionChart(selectedOS);
            document.getElementById('osChart').parentElement.style.display = 'none';
            document.getElementById('osVersionChart').parentElement.style.display = 'block';
            document.getElementById('backButtonOS').style.display = 'inline-block';
          }
        },
        plugins: {
          legend: {
            position: 'bottom'
          },
          title: {
            display: true,
            text: 'Operating System Distribution'
          },
          tooltip: {
            callbacks: {
              label: context => {
                const value = context.parsed;
                const percentage = ((value / total) * 100).toFixed(1);
                return `${context.label}: ${value} (${percentage}%)`;
              }
            }
          },
          datalabels: {
            color: '#000',
            font: { weight: 'bold', size: 12 },
            formatter: value => `${((value / total) * 100).toFixed(0)}%`
          }
        }
      },
      plugins: [ChartDataLabels]
    });
  });


// 6. Load OS Version Stats
fetch('http://localhost:8080/api/os-version-stats')
  .then(res => res.json())
  .then(data => osVersionStatsData = data);

// 7. Render OS Version Chart
function renderOSVersionChart(osName) {
  const filteredData = osVersionStatsData.filter(d => d.os === osName);
  const labels = filteredData.map(item => item.osVersion);
  const counts = filteredData.map(item => item.count);

  const ctx = document.getElementById('osVersionChart').getContext('2d');
  if (osVersionChartInstance) osVersionChartInstance.destroy();

  osVersionChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels,
      datasets: [{
        label: `Versions of ${osName}`,
        data: counts,
        backgroundColor: '#66BB6A'
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: { display: false },
        title: {
          display: true,
          text: `OS Versions for ${osName}`
        }
      }
    }
  });
}

// 8. Back to OS View
document.getElementById('backButtonOS').addEventListener('click', () => {
  document.getElementById('osVersionChart').parentElement.style.display = 'none';
  document.getElementById('osChart').parentElement.style.display = 'block';
  document.getElementById('backButtonOS').style.display = 'none';
});

// // 9. Hourly Access Trend
fetch('http://localhost:8080/api/hourly-hits')
  .then(res => res.json())
  .then(data => {
    const labels = data.map(item => `${item.hour}:00`);
    const counts = data.map(item => item.count);

    const ctx = document.getElementById('hourlyChart').getContext('2d');
    hourlyChartInstance = new Chart(ctx, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: 'Hits per Hour',
          data: counts,
          borderColor: '#FF6384',
          fill: false,
          tension: 0.3,
          pointRadius: 4,
          pointBackgroundColor: '#FF6384'
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: 'Hourly Access Pattern'
          },
          tooltip: {
            callbacks: {
              // Tooltip label customization
              title: function(tooltipItems) {
                const hour = parseInt(tooltipItems[0].label.split(':')[0]);
                const ampm = hour >= 12 ? 'PM' : 'AM';
                const hour12 = hour % 12 === 0 ? 12 : hour % 12;
                return `${hour12}:00 ${ampm}`;
              }
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  });