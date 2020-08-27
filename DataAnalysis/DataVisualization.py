# -*- coding: utf-8 -*-
"""
Created on Sat Apr  4 19:14:42 2020

@author: User
"""

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
location = "DataSet/5Days, 50 Hospitals/"
d1 = pd.read_csv(location+"/D1.csv")
d2 = pd.read_csv(str(location)+"D2.csv")
d3 = pd.read_csv(str(location)+"D3.csv")
d4 = pd.read_csv(str(location)+"D4.csv")
d5 = pd.read_csv(str(location)+"D5.csv")

concatData = pd.concat([d1, d2, d3, d4, d5], axis=1, keys=['d1', 'd2','d3','d4','d5'])
averageData = concatData.swaplevel(0, 1, axis=1)
averageData = averageData.groupby(level=0, axis=1).mean()
averageData['tick'] = averageData['tick']/2
plt.plot(averageData['tick'], averageData['Normal_Count'], color='blue', label = 'Susceptible')
plt.plot(averageData['tick'], averageData['Infected_Count'], color ='red', label = 'Infected' )
plt.plot(averageData['tick'], averageData['Recovered_Count'], color='green', label = 'Recovered' )
plt.plot(averageData['tick'], averageData['Dead_Count'], color= 'black', label = 'Dead' )
#plt.title("Social Isolation Started From 10th Day \n  Hospital Capacity=10 Beds")
plt.xlabel("Number of Days")
plt.ylabel('Number of People')
plt.legend()
plt.savefig(str(location)+"fig12.png")
plt.show()
averageData.to_csv(str(location)+"/averagValue.csv", index=False)