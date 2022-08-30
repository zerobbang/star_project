# -*- coding: utf-8 -*-

import numpy as np # linear algebra
import pandas as pd # data processing, CSV file I/O (e.g. pd.read_csv)

print("numpy version:", np.__version__)
print("pandas version:", pd.__version__)
#!pip install https://github.com/pandas-profiling/pandas-profiling/archive/master.zip

#from pandas_profiling import ProfileReport

# Input data files are available in the read-only "../input/" directory
# For example, running this (by clicking run or pressing Shift+Enter) will list all files under the input directory

import os

# 데이터 가공 


# 데이터 시각화
import matplotlib
import matplotlib.pyplot as plt 
#import seaborn as sns 
print("matplotlib version:", matplotlib.__version__)
#print("seaborn version:", sns.__version__)

# 모델
from sklearn.metrics import accuracy_score
from sklearn.model_selection import StratifiedKFold
from sklearn.model_selection import cross_validate
from sklearn.model_selection import train_test_split, GridSearchCV, RandomizedSearchCV, StratifiedKFold
from sklearn.linear_model import LogisticRegression
from sklearn.neighbors import KNeighborsClassifier
from sklearn.svm import SVC
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
# pip install xgboost
# pip install lightgbm
# pip install sklearn
from xgboost import XGBClassifier
from lightgbm import LGBMClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import OneHotEncoder
from sklearn.compose import make_column_transformer
from sklearn.impute import SimpleImputer
from sklearn.metrics import confusion_matrix

import time

# 서울 2018 ~ 2020년 매일 날씨 데이터
dir = os.path.dirname(os.path.realpath(__file__))
seoul2018 = pd.read_csv(dir+"/need_files/SURFACE_ASOS_108_HR_2018_2018_2019.csv", encoding='cp949')
seoul2019 = pd.read_csv(dir +"/need_files/SURFACE_ASOS_108_HR_2019_2019_2020.csv", encoding='cp949')
seoul2020 = pd.read_csv(dir +"/need_files/SURFACE_ASOS_108_HR_2020_2020_2021.csv", encoding='cp949')



# 서울 2018 ~ 2020년 매일 미세 먼지 데이터

mi2018 = pd.read_csv(dir+"/need_files/ENV_YDST_108_HR_2018_2018_2019.csv", encoding='cp949')
mi2019 = pd.read_csv(dir+"/need_files/ENV_YDST_108_HR_2019_2019_2020.csv", encoding='cp949')
mi2020 = pd.read_csv(dir+"/need_files/ENV_YDST_108_HR_2020_2020_2021.csv", encoding='cp949')
 
# 중국 2018 ~ 2020년 미세먼지 데이터
sandong_2020= pd.read_csv(dir+"/need_files/china_2020_sangdong.csv", encoding='cp949')
sandong_2020_before=pd.read_csv(dir+"/need_files/china_2020_sangdong_before.csv", encoding='cp949')
sandong_2019_before=pd.read_csv(dir+"/need_files/china_2019_sangdong_before.csv", encoding='cp949')
sandong_2018_before=pd.read_csv(dir+"/need_files/china_2018_sangdong_before.csv", encoding='cp949')

# 서울 기상 데이터에서 필요한 값들만 불러 오기
se20 = seoul2020[['일시','풍속(m/s)','풍향(16방위)','습도(%)','강수량(mm)']]
se19 = seoul2019[['일시','풍속(m/s)','풍향(16방위)','습도(%)','강수량(mm)']]
se18 = seoul2018[['일시','풍속(m/s)','풍향(16방위)','습도(%)','강수량(mm)']]

# 산둥성 지방의 미세먼지 하루별 미세먼지 데이터 불러오기
# Unnamed: 0 이라는 index 비슷한 컬럼이 있어서 날려버림
# sandong_2020_before는 우리가 영향을 받는것은 당장의 데이터 보다는 하루 전의 데이터일 확률이 높아서
# 24시간 전의 데이터를 before로 따로 정리해놓은 파일임.
sandong_2020 = sandong_2020.drop('Unnamed: 0' , axis=1)
sandong_2020_before = sandong_2020_before.drop('Unnamed: 0', axis=1)
sandong_2019_before = sandong_2019_before.drop('Unnamed: 0', axis=1)
sandong_2018_before = sandong_2018_before.drop('Unnamed: 0', axis=1)
# 필요없는 Unnamed: 0 열 삭제

# se20과 mi2020의 일시 데이터를 datetime format으로 변경
se20['일시'] = pd.to_datetime(se20['일시'], format='%Y-%m-%d %H:%M', errors='raise')
mi2020['일시'] = pd.to_datetime(mi2020['일시'], format='%Y-%m-%d %H:%M', errors='raise')
se19['일시'] = pd.to_datetime(se19['일시'], format='%Y-%m-%d %H:%M', errors='raise')
mi2019['일시'] = pd.to_datetime(mi2019['일시'], format='%Y-%m-%d %H:%M', errors='raise')
se18['일시'] = pd.to_datetime(se18['일시'], format='%Y-%m-%d %H:%M', errors='raise')
mi2018['일시'] = pd.to_datetime(mi2018['일시'], format='%Y-%m-%d %H:%M', errors='raise')

result2020 = pd.merge(se20, mi2020,"left",on ="일시")
result2019 = pd.merge(se19, mi2019, "left", on="일시")
result2018 = pd.merge(se18, mi2018, "left", on="일시")
# se20과 mi2020 데이터를 조인해서 result2020를 만듬
result2020= result2020.drop('지점', axis=1)
result2019 = result2019.drop('지점', axis=1)
result2018 = result2018.drop('지점', axis=1)
# 필요없어진 지점(어차피 서울을 뜻하는 108이기 때문에) 컬럼을 날려버림

#result2020.isnull().sum()
#result2019.isnull().sum()
#result2018.isnull().sum()

# 중국데이터도 합쳐서 result0라는 하나의 데이터프레임으로 만들었다.
# 아래 코드는 합치는 과정에서 중국은 24시간마다 기록되어있기 때문에 우리나라의 1시간마다의 데이터에 같은 날짜의 중국 데이터 24개를
# 모두 채워넣는 코드임.
result2020['date']= result2020['일시'].dt.strftime('%Y-%m-%d')
result2020['date'] = pd.to_datetime(result2020['date'], format = '%Y-%m-%d')
result2019['date']= result2019['일시'].dt.strftime('%Y-%m-%d')
result2019['date'] = pd.to_datetime(result2019['date'], format = '%Y-%m-%d')
result2018['date']= result2018['일시'].dt.strftime('%Y-%m-%d')
result2018['date'] = pd.to_datetime(result2018['date'], format = '%Y-%m-%d')
#sandong_2020['date'] = pd.to_datetime(sandong_2020['date'],format='%Y-%m-%d %H:%M', errors='raise')
sandong_2020_before['date'] = pd.to_datetime(sandong_2020_before['date'],format='%Y-%m-%d %H:%M', errors='raise')
sandong_2020_before.rename(columns = {'mean':'중국 1시간평균 미세먼지농도(㎍/㎥) before'},inplace=True)
sandong_2019_before['date'] = pd.to_datetime(sandong_2019_before['date'],format='%Y-%m-%d %H:%M', errors='raise')
sandong_2019_before.rename(columns = {'mean':'중국 1시간평균 미세먼지농도(㎍/㎥) before'},inplace=True)
sandong_2018_before['date'] = pd.to_datetime(sandong_2018_before['date'],format='%Y-%m-%d %H:%M', errors='raise')
sandong_2018_before.rename(columns = {'mean':'중국 1시간평균 미세먼지농도(㎍/㎥) before'},inplace=True)
#result0 = pd.merge(result2020, sandong_2020, "left", on = 'date')
result0 = pd.merge(result2020, sandong_2020_before, 'left', on='date')
result01 = pd.merge(result2019, sandong_2019_before, 'left', on='date')
result02 = pd.merge(result2018, sandong_2018_before, 'left', on='date')

result0= result0.drop('date', axis=1)
result0.rename(columns = {'mean':'중국 1시간평균 미세먼지농도(㎍/㎥)'},inplace=True)
result01= result01.drop('date', axis=1)
result01.rename(columns = {'mean':'중국 1시간평균 미세먼지농도(㎍/㎥)'},inplace=True)
result02= result02.drop('date', axis=1)
result02.rename(columns = {'mean':'중국 1시간평균 미세먼지농도(㎍/㎥)'},inplace=True)

"""## 결측치 처리"""

mask1 = result0.loc[(result0['일시'] < '2020-04-01')] # ~3월
mask3 = result0.loc[(result0['일시'] >= '2020-11-01')].reset_index(drop=True) # 11월~


# mask1, mask3
# 아예 안와서 Null로 기록된 값과, 엄청 적게와서 0.0으로 기록된 값을 분류하기 위해
# 임의로 0.0 대신 0.09 라고 생각하고, 아예 안 온 Null 값은 0.0으로 대체함.
# 겨울철은 3시간마다 한번씩 기록하기 때문에, 값, Nan, Nan이 반복되므로 맨처음 값을 3으로 나누어 3칸에 입력
# 0.0의 경우 0.09라고 생각하기로 하였으므로 3으로 나눈 0.03을 3칸에 나누어 기록하게 함
# 0.0 Nan Nan => 0.03 0.03 0.03
i = 0
while(True):

  if i >= len(mask1['일시']):
    mask1['강수량(mm)'] = mask1['강수량(mm)'].fillna(0.0)
    #print('mask1 종료')
    break
  elif (np.isnan(mask1['강수량(mm)'][i])==False) and (np.isnan(mask1['강수량(mm)'][i+1])==True) and (np.isnan(mask1['강수량(mm)'][i+2])==True):
    # 있없없
    if(mask1['강수량(mm)'][i] == 0.0):
      mask1['강수량(mm)'][i] = 0.03
      mask1['강수량(mm)'][i+1] = 0.03
      mask1['강수량(mm)'][i+2] = 0.03
      i=i+3
    else:  
      mask1['강수량(mm)'][i] = round(mask1['강수량(mm)'][i]/3,2)
      mask1['강수량(mm)'][i+1] = mask1['강수량(mm)'][i]
      mask1['강수량(mm)'][i+2] = mask1['강수량(mm)'][i]
      i=i+3
  elif (np.isnan(mask1['강수량(mm)'][i])==True) and (np.isnan(mask1['강수량(mm)'][i+1])==True) and (np.isnan(mask1['강수량(mm)'][i+2])==True):
    mask1['강수량(mm)'][i] = 0.0
    mask1['강수량(mm)'][i+1] = 0.0
    mask1['강수량(mm)'][i+2] = 0.0
    i=i+3


i = 0
while(True):
  
  if (np.isnan(mask3['강수량(mm)'][i])):
    
    i=i+1
  else:
    break
  
while(True):
    
    
    if i >= len(mask3['일시']):
      mask3['강수량(mm)'] = mask3['강수량(mm)'].fillna(0.0)
      #print('mask3 종료')
      break
    


    elif (np.isnan(mask3['강수량(mm)'][i])==False) and (np.isnan(mask3['강수량(mm)'][i+1])==True) and (np.isnan(mask3['강수량(mm)'][i+2])==True):
      # 있없없
      if(mask3['강수량(mm)'][i] == 0.0):
        mask3['강수량(mm)'][i] = 0.03
        mask3['강수량(mm)'][i+1] = 0.03
        mask3['강수량(mm)'][i+2] = 0.03
      
        i=i+3
      else:  
        mask3['강수량(mm)'][i] = round((mask3['강수량(mm)'][i])/3,2)
        mask3['강수량(mm)'][i+1] = mask3['강수량(mm)'][i]
        mask3['강수량(mm)'][i+2] = mask3['강수량(mm)'][i]
    
        i=i+3
    elif (np.isnan(mask3['강수량(mm)'][i])==True) and (np.isnan(mask3['강수량(mm)'][i+1])==True) and (np.isnan(mask3['강수량(mm)'][i+2])==True):
      mask3['강수량(mm)'][i] = 0.0
      mask3['강수량(mm)'][i+1] = 0.0
      mask3['강수량(mm)'][i+2] = 0.0
      
      i=i+3
  

mask2 = result0.loc[((result0['일시'] >= '2020-04-01') & (result0['일시']< '2020-11-01' ))].reset_index(drop=True)

for i in range(len(mask2['강수량(mm)'])):
  if (np.isnan(mask2['강수량(mm)'][i])== False) and (mask2['강수량(mm)'][i] == 0.0) :
    mask2['강수량(mm)'][i] = 0.03
mask2['강수량(mm)'] = mask2['강수량(mm)'].fillna(0.0)

result1 = pd.concat([mask1, mask2], ignore_index=True)
result0 = pd.concat([result1, mask3], ignore_index=True)
result0.isnull().sum()

## result0으로 mask1,2,3을 합치고 null 갯수확인
## 강수량 결측치 갯수 0으로 결측치 처리 완료 됐음을 확인

# 강수량 결측치 처리 - result01 (2019년)으로

# 강수량은 겨울철(11월~익년 3월) 3시간 간격으로 제공
# 겨울철 강수량은 3시간마다 한번씩만 측정되고, 첫한시간에 값이 들어간 후 이어지는 2시간은 Nan값이 입력되어 있으므로 
# 3시간마다의 처음값을 /3 하여 세칸에 나누어 입력함.

# 동일한 기준을 가지는 1~3월까지와(= mask1), 11~12월의 데이터(= mask3)를 분류하고 결측치를 채운다.

mask1 = result01.loc[(result01['일시'] < '2019-04-01')] # ~3월
mask3 = result01.loc[(result01['일시'] >= '2019-11-01')].reset_index(drop=True) # 11월~
# mask2 = result0.loc[((result0['일시'] >= '2020-04-01') & (result0['일시']< '2020-11-01' ))].reset_index(drop=True) # 4~10월
# mask3 = result0.loc[(result0['일시'] >= '2020-11-01')] # 11월~

# mask1, mask3은 묶어서 처리
# mask2 만 따로 처리


# mask1, mask3
# 아예 안와서 Null로 기록된 값과, 엄청 적게와서 0.0으로 기록된 값을 분류하기 위해
# 임의로 0.0 대신 0.09 라고 생각하고, 아예 안 온 Null 값은 0.0으로 대체함.
# 겨울철은 3시간마다 한번씩 기록하기 때문에, 값, Nan, Nan이 반복되므로 맨처음 값을 3으로 나누어 3칸에 입력
# 0.0의 경우 0.09라고 생각하기로 하였으므로 3으로 나눈 0.03을 3칸에 나누어 기록하게 함
# 0.0 Nan Nan => 0.03 0.03 0.03
i = 0
while(True):

  if i >= len(mask1['일시']):
    mask1['강수량(mm)'] = mask1['강수량(mm)'].fillna(0.0)
    print('mask1 종료')
    break
  elif (np.isnan(mask1['강수량(mm)'][i])==False) and (np.isnan(mask1['강수량(mm)'][i+1])==True) and (np.isnan(mask1['강수량(mm)'][i+2])==True):
    # 있없없
    if(mask1['강수량(mm)'][i] == 0.0):
      mask1['강수량(mm)'][i] = 0.03
      mask1['강수량(mm)'][i+1] = 0.03
      mask1['강수량(mm)'][i+2] = 0.03
      i=i+3
    else:  
      mask1['강수량(mm)'][i] = round(mask1['강수량(mm)'][i]/3,2)
      mask1['강수량(mm)'][i+1] = mask1['강수량(mm)'][i]
      mask1['강수량(mm)'][i+2] = mask1['강수량(mm)'][i]
      i=i+3
  elif (np.isnan(mask1['강수량(mm)'][i])==True) and (np.isnan(mask1['강수량(mm)'][i+1])==True) and (np.isnan(mask1['강수량(mm)'][i+2])==True):
    mask1['강수량(mm)'][i] = 0.0
    mask1['강수량(mm)'][i+1] = 0.0
    mask1['강수량(mm)'][i+2] = 0.0
    i=i+3


i = 0
while(True):
  
  if (np.isnan(mask3['강수량(mm)'][i])):
    
    i=i+1
  else:
    break
  
while(True):
    
    
    if i >= len(mask3['일시']):
      mask3['강수량(mm)'] = mask3['강수량(mm)'].fillna(0.0)
      print('mask3 종료')
      break
    


    elif (np.isnan(mask3['강수량(mm)'][i])==False) and (np.isnan(mask3['강수량(mm)'][i+1])==True) and (np.isnan(mask3['강수량(mm)'][i+2])==True):
      # 있없없
      if(mask3['강수량(mm)'][i] == 0.0):
        mask3['강수량(mm)'][i] = 0.03
        mask3['강수량(mm)'][i+1] = 0.03
        mask3['강수량(mm)'][i+2] = 0.03
      
        i=i+3
      else:  
        mask3['강수량(mm)'][i] = round((mask3['강수량(mm)'][i])/3,2)
        mask3['강수량(mm)'][i+1] = mask3['강수량(mm)'][i]
        mask3['강수량(mm)'][i+2] = mask3['강수량(mm)'][i]
    
        i=i+3
    elif (np.isnan(mask3['강수량(mm)'][i])==True) and (np.isnan(mask3['강수량(mm)'][i+1])==True) and (np.isnan(mask3['강수량(mm)'][i+2])==True):
      mask3['강수량(mm)'][i] = 0.0
      mask3['강수량(mm)'][i+1] = 0.0
      mask3['강수량(mm)'][i+2] = 0.0
      
      i=i+3

# result01 (2019년)- mask2 처리

# mask1, mask3 제외한 나머지 기간을 mask2로 묶어서 처리함
# Nan 값이 아니고 0.0인 경우 임의의 값 0.03으로 동일하게 처리
# 그 외 Nan 값의 경우 0.0으로 처리
mask2 = result01.loc[((result01['일시'] >= '2019-04-01') & (result01['일시']< '2019-11-01' ))].reset_index(drop=True)


for i in range(len(mask2['강수량(mm)'])):
  if (np.isnan(mask2['강수량(mm)'][i])== False) and (mask2['강수량(mm)'][i] == 0.0) :
    mask2['강수량(mm)'][i] = 0.03

mask2['강수량(mm)'] = mask2['강수량(mm)'].fillna(0.0)

result1 = pd.concat([mask1, mask2], ignore_index=True)
result01 = pd.concat([result1, mask3], ignore_index=True)
result01.isnull().sum()

## result01으로 mask1,2,3을 합치고 null 갯수확인
## 강수량 결측치 갯수 0으로 결측치 처리 완료 됐음을 확인

# 강수량 결측치 처리 - result02 (2018년)으로

# 강수량은 겨울철(11월~익년 3월) 3시간 간격으로 제공
# 겨울철 강수량은 3시간마다 한번씩만 측정되고, 첫한시간에 값이 들어간 후 이어지는 2시간은 Nan값이 입력되어 있으므로 
# 3시간마다의 처음값을 /3 하여 세칸에 나누어 입력함.

# 동일한 기준을 가지는 1~3월까지와(= mask1), 11~12월의 데이터(= mask3)를 분류하고 결측치를 채운다.

mask1 = result02.loc[(result02['일시'] < '2018-04-01')] # ~3월
mask3 = result02.loc[(result02['일시'] >= '2018-11-01')].reset_index(drop=True) # 11월~
# mask2 = result0.loc[((result0['일시'] >= '2020-04-01') & (result0['일시']< '2020-11-01' ))].reset_index(drop=True) # 4~10월
# mask3 = result0.loc[(result0['일시'] >= '2020-11-01')] # 11월~

# mask1, mask3은 묶어서 처리
# mask2 만 따로 처리


# mask1, mask3
# 아예 안와서 Null로 기록된 값과, 엄청 적게와서 0.0으로 기록된 값을 분류하기 위해
# 임의로 0.0 대신 0.09 라고 생각하고, 아예 안 온 Null 값은 0.0으로 대체함.
# 겨울철은 3시간마다 한번씩 기록하기 때문에, 값, Nan, Nan이 반복되므로 맨처음 값을 3으로 나누어 3칸에 입력
# 0.0의 경우 0.09라고 생각하기로 하였으므로 3으로 나눈 0.03을 3칸에 나누어 기록하게 함
# 0.0 Nan Nan => 0.03 0.03 0.03
i = 0
while(True):

  if i >= len(mask1['일시']):
    mask1['강수량(mm)'] = mask1['강수량(mm)'].fillna(0.0)
    print('mask1 종료')
    break
  elif (np.isnan(mask1['강수량(mm)'][i])==False) and (np.isnan(mask1['강수량(mm)'][i+1])==True) and (np.isnan(mask1['강수량(mm)'][i+2])==True):
    # 있없없
    if(mask1['강수량(mm)'][i] == 0.0):
      mask1['강수량(mm)'][i] = 0.03
      mask1['강수량(mm)'][i+1] = 0.03
      mask1['강수량(mm)'][i+2] = 0.03
      i=i+3
    else:  
      mask1['강수량(mm)'][i] = round(mask1['강수량(mm)'][i]/3,2)
      mask1['강수량(mm)'][i+1] = mask1['강수량(mm)'][i]
      mask1['강수량(mm)'][i+2] = mask1['강수량(mm)'][i]
      i=i+3
  elif (np.isnan(mask1['강수량(mm)'][i])==True) and (np.isnan(mask1['강수량(mm)'][i+1])==True) and (np.isnan(mask1['강수량(mm)'][i+2])==True):
    mask1['강수량(mm)'][i] = 0.0
    mask1['강수량(mm)'][i+1] = 0.0
    mask1['강수량(mm)'][i+2] = 0.0
    i=i+3


i = 0
while(True):
  
  if (np.isnan(mask3['강수량(mm)'][i])):
    
    i=i+1
  else:
    break
  
while(True):
    
    
    if i >= len(mask3['일시']):
      mask3['강수량(mm)'] = mask3['강수량(mm)'].fillna(0.0)
      print('mask3 종료')
      break
    


    elif (np.isnan(mask3['강수량(mm)'][i])==False) and (np.isnan(mask3['강수량(mm)'][i+1])==True) and (np.isnan(mask3['강수량(mm)'][i+2])==True):
      # 있없없
      if(mask3['강수량(mm)'][i] == 0.0):
        mask3['강수량(mm)'][i] = 0.03
        mask3['강수량(mm)'][i+1] = 0.03
        mask3['강수량(mm)'][i+2] = 0.03
      
        i=i+3
      else:  
        mask3['강수량(mm)'][i] = round((mask3['강수량(mm)'][i])/3,2)
        mask3['강수량(mm)'][i+1] = mask3['강수량(mm)'][i]
        mask3['강수량(mm)'][i+2] = mask3['강수량(mm)'][i]
    
        i=i+3
    elif (np.isnan(mask3['강수량(mm)'][i])==True) and (np.isnan(mask3['강수량(mm)'][i+1])==True) and (np.isnan(mask3['강수량(mm)'][i+2])==True):
      mask3['강수량(mm)'][i] = 0.0
      mask3['강수량(mm)'][i+1] = 0.0
      mask3['강수량(mm)'][i+2] = 0.0
      
      i=i+3

# result02 (2018년)- mask2 처리

# mask1, mask3 제외한 나머지 기간을 mask2로 묶어서 처리함
# Nan 값이 아니고 0.0인 경우 임의의 값 0.03으로 동일하게 처리
# 그 외 Nan 값의 경우 0.0으로 처리
mask2 = result02.loc[((result02['일시'] >= '2018-04-01') & (result02['일시']< '2018-11-01' ))].reset_index(drop=True)


for i in range(len(mask2['강수량(mm)'])):
  if (np.isnan(mask2['강수량(mm)'][i])== False) and (mask2['강수량(mm)'][i] == 0.0) :
    mask2['강수량(mm)'][i] = 0.03

mask2['강수량(mm)'] = mask2['강수량(mm)'].fillna(0.0)

result1 = pd.concat([mask1, mask2], ignore_index=True)
result02 = pd.concat([result1, mask3], ignore_index=True)
result02.isnull().sum()

## result02으로 mask1,2,3을 합치고 null 갯수확인
## 강수량 결측치 갯수 0으로 결측치 처리 완료 됐음을 확인

result03 = pd.concat([result02,result01], ignore_index=True )
result0 = pd.concat([result03, result0], ignore_index=True)
result0



result0['풍향(16방위)'] = result0['풍향(16방위)'].fillna(result0['풍향(16방위)'].interpolate())

# 풍향 16방위로 묶기
result0.loc[(result0['풍향(16방위)'] >= 0.0) & (result0['풍향(16방위)'] < 11.25) , '풍향D'] = 0
result0.loc[(result0['풍향(16방위)'] >= 11.25) & (result0['풍향(16방위)'] < 33.75) , '풍향D'] = 22.5
result0.loc[(result0['풍향(16방위)'] >= 33.75) & (result0['풍향(16방위)'] < 56.25) , '풍향D'] = 45
result0.loc[(result0['풍향(16방위)'] >= 56.25) & (result0['풍향(16방위)'] < 78.75) , '풍향D'] = 67.5
result0.loc[(result0['풍향(16방위)'] >= 78.75) & (result0['풍향(16방위)'] < 101.25) , '풍향D'] = 90
result0.loc[(result0['풍향(16방위)'] >= 101.25) & (result0['풍향(16방위)'] < 123.75) , '풍향D'] = 112.5
result0.loc[(result0['풍향(16방위)'] >= 123.75) & (result0['풍향(16방위)'] < 146.25) , '풍향D'] = 135
result0.loc[(result0['풍향(16방위)'] >= 146.25) & (result0['풍향(16방위)'] < 168.75) , '풍향D'] = 157.5
result0.loc[(result0['풍향(16방위)'] >= 168.75) & (result0['풍향(16방위)'] < 191.25) , '풍향D'] = 180
result0.loc[(result0['풍향(16방위)'] >= 191.25) & (result0['풍향(16방위)'] < 213.75) , '풍향D'] = 202.5
result0.loc[(result0['풍향(16방위)'] >= 213.75) & (result0['풍향(16방위)'] < 236.25) , '풍향D'] = 225
result0.loc[(result0['풍향(16방위)'] >= 236.25) & (result0['풍향(16방위)'] < 258.75) , '풍향D'] = 247.5
result0.loc[(result0['풍향(16방위)'] >= 258.75) & (result0['풍향(16방위)'] < 281.25) , '풍향D'] = 270
result0.loc[(result0['풍향(16방위)'] >= 281.25) & (result0['풍향(16방위)'] < 303.75) , '풍향D'] = 292.5
result0.loc[(result0['풍향(16방위)'] >= 303.75) & (result0['풍향(16방위)'] < 326.25) , '풍향D'] = 315
result0.loc[(result0['풍향(16방위)'] >= 326.25) & (result0['풍향(16방위)'] < 348.75) , '풍향D'] = 337.5
result0.loc[(result0['풍향(16방위)'] >=348.75) & (result0['풍향(16방위)'] <= 360) , '풍향D'] = 0


## 시간별 풍속 데이터 결측치 보정
## 만약에 첫번째 값이 결측치 인 경우, 가장 최초로 나오는 결측치 가 아닌 값을 넣는다.
a = 1
while(True):
  if np.isnan(result0['풍속(m/s)'][0]):
    result0['풍속(m/s)'][0] = result0['풍속(m/s)'][a]
    a = a+1
  else:
    break

## 첫번째 이후의 결측치를 보정
## 실측/결측/실측 의 경우 양쪽 실측값들 사이의 평균을 결측치로 보정함
## 실측/ 결측/ 결측의 경우 앞의 실측값을 그대로 불러와서 보정함
## 맨 마지막 값이 결측치 인 경우 그 직전값을 불러와서 보정함.
for i in range(len(result0['풍속(m/s)'])):
  i = i+1
  if i == len(result0['풍속(m/s)'])-1:
    if np.isnan(result0['풍속(m/s)'][i]):
      result0['풍속(m/s)'][i] = result0['풍속(m/s)'][i-1]
    break
  elif np.isnan(result0['풍속(m/s)'][i]):
    if np.isnan(result0['풍속(m/s)'][i+1]):
      result0['풍속(m/s)'][i] = result0['풍속(m/s)'][i-1]
    else:
      result0['풍속(m/s)'][i] = (result0['풍속(m/s)'][i-1]+result0['풍속(m/s)'][i+1])/2

## 시간별 습도 데이터 결측치 보정
## 만약에 첫번째 값이 결측치 인 경우, 가장 최초로 나오는 결측치 가 아닌 값을 넣는다.
a = 1
while(True):
  if np.isnan(result0['습도(%)'][0]):
    result0['습도(%)'][0] = result0['습도(%)'][a]
    a = a+1
  else:
    break

## 첫번째 이후의 결측치를 보정
## 실측/결측/실측 의 경우 양쪽 실측값들 사이의 평균을 결측치로 보정함
## 실측/ 결측/ 결측의 경우 앞의 실측값을 그대로 불러와서 보정함
## 맨 마지막 값이 결측치 인 경우 그 직전값을 불러와서 보정함.
for i in range(len(result0['습도(%)'])):
  i = i+1
  if i == len(result0['습도(%)'])-1:
    if np.isnan(result0['습도(%)'][i]):
      result0['습도(%)'][i] = result0['습도(%)'][i-1]
    break
  elif np.isnan(result0['습도(%)'][i]):
    if np.isnan(result0['습도(%)'][i+1]):
      result0['습도(%)'][i] = result0['습도(%)'][i-1]
    else:
      result0['습도(%)'][i] = (result0['습도(%)'][i-1]+result0['습도(%)'][i+1])/2



## 시간별 미세먼지 농도 데이터 결측치 보정

## 만약에 첫번째 값이 결측치 인 경우, 가장 최초로 나오는 결측치 가 아닌 값을 넣는다.
a = 1
while(True):
  if np.isnan(result0['1시간평균 미세먼지농도(㎍/㎥)'][0]):
    result0['1시간평균 미세먼지농도(㎍/㎥)'][0] = result0['1시간평균 미세먼지농도(㎍/㎥)'][a]
    a = a+1
  else:
    break

## 첫번째 이후의 결측치를 보정
## 실측/결측/실측 의 경우 양쪽 실측값들 사이의 평균을 결측치로 보정함
## 실측/ 결측/ 결측의 경우 앞의 실측값을 그대로 불러와서 보정함
## 맨 마지막 값이 결측치 인 경우 그 직전값을 불러와서 보정함.
for i in range(len(result0['1시간평균 미세먼지농도(㎍/㎥)'])):
  i = i+1
  if i == len(result0['1시간평균 미세먼지농도(㎍/㎥)'])-1:
    if np.isnan(result0['1시간평균 미세먼지농도(㎍/㎥)'][i]):
      result0['1시간평균 미세먼지농도(㎍/㎥)'][i] = result0['1시간평균 미세먼지농도(㎍/㎥)'][i-1]
    break
  elif np.isnan(result0['1시간평균 미세먼지농도(㎍/㎥)'][i]):
    if np.isnan(result0['1시간평균 미세먼지농도(㎍/㎥)'][i+1]):
      result0['1시간평균 미세먼지농도(㎍/㎥)'][i] = result0['1시간평균 미세먼지농도(㎍/㎥)'][i-1]
    else:
      result0['1시간평균 미세먼지농도(㎍/㎥)'][i] = (result0['1시간평균 미세먼지농도(㎍/㎥)'][i-1]+result0['1시간평균 미세먼지농도(㎍/㎥)'][i+1])/2
for i in range(len(result0['1시간평균 미세먼지농도(㎍/㎥)'])):
  i = i+1
  if i == len(result0['1시간평균 미세먼지농도(㎍/㎥)'])-1:
    if np.isnan(result0['1시간평균 미세먼지농도(㎍/㎥)'][i]):
      result0['1시간평균 미세먼지농도(㎍/㎥)'][i] = result0['1시간평균 미세먼지농도(㎍/㎥)'][i-1]
    break
  elif np.isnan(result0['1시간평균 미세먼지농도(㎍/㎥)'][i]):
    if np.isnan(result0['1시간평균 미세먼지농도(㎍/㎥)'][i+1]):
      result0['1시간평균 미세먼지농도(㎍/㎥)'][i] = result0['1시간평균 미세먼지농도(㎍/㎥)'][i-1]
    else:
      result0['1시간평균 미세먼지농도(㎍/㎥)'][i] = (result0['1시간평균 미세먼지농도(㎍/㎥)'][i-1]+result0['1시간평균 미세먼지농도(㎍/㎥)'][i+1])/2

#result0.isnull().sum()

result0 = result0.astype({'1시간평균 미세먼지농도(㎍/㎥)':'int','중국 1시간평균 미세먼지농도(㎍/㎥) before':'int' })

## 미세먼지 평가 별로 데이터를 분류했슴다.(나중에 가치 판단할 때 쓰기위함)

result0.loc[(result0['1시간평균 미세먼지농도(㎍/㎥)'] >= 0.0) & (result0['1시간평균 미세먼지농도(㎍/㎥)'] < 31) , '미세먼지 평가'] = '좋음'
result0.loc[(result0['1시간평균 미세먼지농도(㎍/㎥)'] >= 31) & (result0['1시간평균 미세먼지농도(㎍/㎥)'] < 81) , '미세먼지 평가'] = '보통'
result0.loc[(result0['1시간평균 미세먼지농도(㎍/㎥)'] >= 81) & (result0['1시간평균 미세먼지농도(㎍/㎥)'] < 151) , '미세먼지 평가'] = '나쁨'
result0.loc[(result0['1시간평균 미세먼지농도(㎍/㎥)'] >= 151) , '미세먼지 평가'] = '매우 나쁨'

#result0
#result0.info()

# 원 핫 인코딩 진행(풍향D)

result0_onehot= pd.get_dummies(data = result0, columns= ['풍향D'])
result0_onehot
result0_onehot['미세먼지 평가'] = result0_onehot['미세먼지 평가'].map({'좋음':0, '보통':1, '나쁨':2,'매우 나쁨':3})
# 좋음:0, 보통:1, 나쁨:2, 매우나쁨:3

# # 풍속 1씩 카테고리화(1단위)

# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 0.0) & (result0_onehot['풍속(m/s)'] < 1.0) , '풍속c'] = 0
# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 1.0) & (result0_onehot['풍속(m/s)'] < 2.0) , '풍속c'] = 1
# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 2.0) & (result0_onehot['풍속(m/s)'] < 3.0) , '풍속c'] = 2
# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 3.0) & (result0_onehot['풍속(m/s)'] < 4.0) , '풍속c'] = 3
# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 4.0) & (result0_onehot['풍속(m/s)'] < 5.0) , '풍속c'] = 4
# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 5.0) & (result0_onehot['풍속(m/s)'] < 6.0) , '풍속c'] = 5
# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 6.0) & (result0_onehot['풍속(m/s)'] < 7.0) , '풍속c'] = 6
# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 7) & (result0_onehot['풍속(m/s)'] < 8.0) , '풍속c'] = 7
# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 8) & (result0_onehot['풍속(m/s)'] < 9.0) , '풍속c'] = 8
# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 9) & (result0_onehot['풍속(m/s)'] < 10.0) , '풍속c'] = 9
# result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 10) & (result0_onehot['풍속(m/s)'] < 11.0) , '풍속c'] = 10

# result0_onehot

# 풍속 0.5씩 나눠서 카테고리화(0.5단위)
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 0.0) & (result0_onehot['풍속(m/s)'] < 0.5) , '풍속c'] = 0
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 0.5) & (result0_onehot['풍속(m/s)'] < 1.0) , '풍속c'] = 0.5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 1.0) & (result0_onehot['풍속(m/s)'] < 1.5) , '풍속c'] = 1
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 1.5) & (result0_onehot['풍속(m/s)'] < 2.0) , '풍속c'] = 1.5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 2.0) & (result0_onehot['풍속(m/s)'] < 2.5) , '풍속c'] = 2
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 2.5) & (result0_onehot['풍속(m/s)'] < 3.0) , '풍속c'] = 2.5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 3.0) & (result0_onehot['풍속(m/s)'] < 3.5) , '풍속c'] = 3
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 3.5) & (result0_onehot['풍속(m/s)'] < 4.0) , '풍속c'] = 3.5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 4.0) & (result0_onehot['풍속(m/s)'] < 4.5) , '풍속c'] = 4
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 4.5) & (result0_onehot['풍속(m/s)'] < 5.0) , '풍속c'] = 4.5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 5.0) & (result0_onehot['풍속(m/s)'] < 5.5) , '풍속c'] = 5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 5.5) & (result0_onehot['풍속(m/s)'] < 6.0) , '풍속c'] = 5.5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 6.0) & (result0_onehot['풍속(m/s)'] < 6.5) , '풍속c'] = 6
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 6.5) & (result0_onehot['풍속(m/s)'] < 7.0) , '풍속c'] = 6.5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 7) & (result0_onehot['풍속(m/s)'] < 7.5) , '풍속c'] = 7
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 7.5) & (result0_onehot['풍속(m/s)'] < 8.0) , '풍속c'] = 7.5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 8) & (result0_onehot['풍속(m/s)'] < 8.5) , '풍속c'] = 8
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 8.5) & (result0_onehot['풍속(m/s)'] < 9.0) , '풍속c'] = 8.5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 9) & (result0_onehot['풍속(m/s)'] < 9.5) , '풍속c'] = 9
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 9.5) & (result0_onehot['풍속(m/s)'] < 10.0) , '풍속c'] = 9.5
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 10) & (result0_onehot['풍속(m/s)'] < 10.5) , '풍속c'] = 10
result0_onehot.loc[(result0_onehot['풍속(m/s)'] >= 10.5) & (result0_onehot['풍속(m/s)'] < 11.0) , '풍속c'] = 10.5


# 습도 카테고리화 (5단위) ( ML 정확도를 높이기 위한 일환 -> 실패)

result0_onehot.loc[(result0_onehot['습도(%)'] >= 0) & (result0_onehot['습도(%)'] < 5) , '습도c'] = 0
result0_onehot.loc[(result0_onehot['습도(%)'] >= 5) & (result0_onehot['습도(%)'] < 10) , '습도c'] = 5
result0_onehot.loc[(result0_onehot['습도(%)'] >= 10) & (result0_onehot['습도(%)'] < 15) , '습도c'] = 10
result0_onehot.loc[(result0_onehot['습도(%)'] >= 15) & (result0_onehot['습도(%)'] < 20) , '습도c'] = 15
result0_onehot.loc[(result0_onehot['습도(%)'] >= 20) & (result0_onehot['습도(%)'] < 25) , '습도c'] = 20
result0_onehot.loc[(result0_onehot['습도(%)'] >= 25) & (result0_onehot['습도(%)'] < 30) , '습도c'] = 25
result0_onehot.loc[(result0_onehot['습도(%)'] >= 30) & (result0_onehot['습도(%)'] < 35) , '습도c'] = 30
result0_onehot.loc[(result0_onehot['습도(%)'] >= 35) & (result0_onehot['습도(%)'] < 40) , '습도c'] = 35
result0_onehot.loc[(result0_onehot['습도(%)'] >= 40) & (result0_onehot['습도(%)'] < 45) , '습도c'] = 40
result0_onehot.loc[(result0_onehot['습도(%)'] >= 45) & (result0_onehot['습도(%)'] < 50) , '습도c'] = 45
result0_onehot.loc[(result0_onehot['습도(%)'] >= 50) & (result0_onehot['습도(%)'] < 55) , '습도c'] = 50
result0_onehot.loc[(result0_onehot['습도(%)'] >= 55) & (result0_onehot['습도(%)'] < 60) , '습도c'] = 55
result0_onehot.loc[(result0_onehot['습도(%)'] >= 60) & (result0_onehot['습도(%)'] < 65) , '습도c'] = 60
result0_onehot.loc[(result0_onehot['습도(%)'] >= 65) & (result0_onehot['습도(%)'] < 70) , '습도c'] = 65
result0_onehot.loc[(result0_onehot['습도(%)'] >= 70) & (result0_onehot['습도(%)'] < 75) , '습도c'] = 70
result0_onehot.loc[(result0_onehot['습도(%)'] >= 75) & (result0_onehot['습도(%)'] < 80) , '습도c'] = 75
result0_onehot.loc[(result0_onehot['습도(%)'] >= 80) & (result0_onehot['습도(%)'] < 85) , '습도c'] = 80
result0_onehot.loc[(result0_onehot['습도(%)'] >= 85) & (result0_onehot['습도(%)'] < 90) , '습도c'] = 85
result0_onehot.loc[(result0_onehot['습도(%)'] >= 90) & (result0_onehot['습도(%)'] < 95) , '습도c'] = 90
result0_onehot.loc[(result0_onehot['습도(%)'] >= 95) & (result0_onehot['습도(%)'] < 100) , '습도c'] = 95
result0_onehot.loc[(result0_onehot['습도(%)'] >= 100) ,'습도c'] = 100
#result0_onehot



"""## 머신 러닝 모형 개발

- 교차 검증
- 하이퍼 파라미터 튜닝
"""

#len(result0_onehot.loc[result0_onehot['미세먼지 평가']==3])

# 독립변수, 종속변수 구분
result0_onehot.columns

# X_cols : 학습할 때 이 데이터들을 쓸것이다.
# y : 예측 해야 하는 값

X_cols = [#'일시',
          '풍속(m/s)',
          # '풍향(16방위)',
          '습도(%)',
          '강수량(mm)',
          #'풍속c',
          #'습도c',
          # '전운량(10분위)',
       #'1시간평균 미세먼지농도(㎍/㎥)',
       #'중국 1시간평균 미세먼지농도(㎍/㎥)',
       '중국 1시간평균 미세먼지농도(㎍/㎥) before',
       '풍향D_0.0', '풍향D_22.5', '풍향D_45.0',
       '풍향D_67.5', '풍향D_90.0', '풍향D_112.5', '풍향D_135.0', '풍향D_157.5',
       '풍향D_180.0', '풍향D_202.5', '풍향D_225.0', '풍향D_247.5', '풍향D_270.0',
       '풍향D_292.5', '풍향D_315.0', '풍향D_337.5'
       ]

X = result0_onehot[X_cols].to_numpy()


# y = result0_onehot['1시간평균 미세먼지농도(㎍/㎥)']
y = result0_onehot['미세먼지 평가'].to_numpy()

X_train, X_val, y_train, y_val = train_test_split(X,y,test_size=0.3, random_state=42)
X_train.shape, X_val.shape, y_train.shape, y_val.shape

"""LogisticRegression, KNN, RandomForest, LGBM 으로 진행"""

# 이 모델들을 쓸것이다.
# LogisticRegression : 로지스틱 회귀
# KNN : K-최근접이웃
# RandomForest : 랜덤포레스트 앙상블
# LGBM : 라이트GBM
# 썼는데 LGBM이 결과가 좋아서 LGBM만 남겼읍니다.

# Models

# Classifiers
classifiers = {
   # "LogisticRegression" : LogisticRegression(random_state=0),
    #"KNN" : KNeighborsClassifier(),
    #"RandomForest" : RandomForestClassifier(random_state=0),
    "LGBM" : LGBMClassifier(random_state=0),
     
}

"""그리드 서치를 위한 파라미터 값 입력"""

## 각각 모델들의 파라미터들 입력하는 칸

# LR_grid = {'penalty': ['l2'],
#            'C': [0.25, 0.5, 0.75, 1, 1.25, 1.5],
#            'max_iter': [50, 100, 150]}

#KNN_grid = {'n_neighbors': [3, 5, 7, 9],
#            'p': [1, 2]}


#RF_grid = {'n_estimators': [50,80,100,140,150],
        #'max_depth': [8,9,10,11,13]}

boosted_grid = {'n_estimators': [#20,
                                 #50,
                                 
                                 1200,
                                 #120,
                                 #140,180
                                 ],
        'max_depth': [#3,
                      #5,
                      #7,
                      #10,
                      12,
                      #15
                      ],
        #'num_leaves': [
            
        #],
        'learning_rate': [#0.01,
            #0.1,
                          #0.15,
                          0.2
                          #,0.25
                          #,0.3
                          #,0.4
                          #,0.5
                          #,0.6
                          ],
               'boosting': ['dart'],
               'feature_fraction': [#0.7,
                                    #0.8,
                                     0.9],
               'min_data_in_leaf': [8,#10 #15 #,25
                                    ],
               'verbose': [1],
                'early_stopping_rounds' : [50],
                #'device_type' : ['cuda']
                }
    
    
# Dictionary of all grids
grid = {
    # "LogisticRegression" : LR_grid,
    #"KNN" : KNN_grid,
    #"RandomForest" : RF_grid,
    "LGBM" : boosted_grid
}

"""- 불러온 모형과 그리드 서치를 각각 개별적으로 정의하는 코드 작성
- 데이터 프레임에 결과를 저장
- 설정한 파라미터에 의해 수행한 모델링 값들 중 가장 좋은 결과를 낸 것을 도출함
"""

## 위에서 정한 모델과 파라미터로 스킷런(ML) 돌립니다.

# Sklearn


i=0
clf_best_params=classifiers.copy()
valid_scores=pd.DataFrame({'Classifer':classifiers.keys(), 'Validation accuracy': np.zeros(len(classifiers)), 'Training time': np.zeros(len(classifiers))})
for key, classifier in classifiers.items():
    start = time.time()
    clf = GridSearchCV(estimator=classifier, param_grid=grid[key], n_jobs=-1, cv=None )

    # Train and score
    clf.fit(X_train, y_train)
    y_hat = clf.predict(X_train)
    scores_df = pd.DataFrame(clf.cv_results_)

    df = pd.DataFrame(np.vstack([y_hat, y_train]).T,columns=["예측","정답"])
    
    valid_scores.iloc[i,1]=clf.score(X_val, y_val)
    
    # Save trained model
    clf_best_params[key]=clf.best_params_
    
    # Print iteration and training time
    stop = time.time()
    valid_scores.iloc[i,2]=np.round((stop - start)/60, 2)
    
    print('Model:', key)
    print('Training time (mins):', valid_scores.iloc[i,2])
    print('')
    i+=1

# 모델 별 측정 결과
valid_scores

#예측한 꼴 보기,,,
print(df)
df.to_csv("/content/drive/MyDrive/Colab Notebooks/dust/예측결과.csv", encoding='utf-8')


pd.DataFrame(X_train).to_csv('train_01.csv')

pd.DataFrame(X_train).to_csv('/content/drive/MyDrive/Colab Notebooks/dust/train_01.csv')

import pandas as pd
test_data = {"예측": [1,3,0,1,2,1,1,0,1,3,0,1,0], "실제":[0,2,2,0,2,1,0,1,1,2,2,1,0]}
pd.DataFrame(test_data).T

clf_best_params

"""혼동 행렬

TP + TN = 정답

FP = 좋을거라 예측했는데 실제로는 나빴던 값

FN = 나쁠거라 예측했는데 좋았던 값

-> 0123 으로 나누어서 혼동행렬을 만들었습니다.

0: 좋음
1: 보통
2: 나쁨
3: 매우나쁨

Actual value: 실제값

Predicted value: 모델이 예측한 값

가운데 대각선에서 멀수록 택도없이 예측한 것입니다.

저희는 그래도 가운데 대각선에 모여있고, 1칸정도씩만 떨어진 곳에 데이터가 많이 분포되어있으니 영 바보같은 예측모델은 아니라고 생각하시면 됩니다.
"""


sns.heatmap(confusion_matrix(y_train, y_hat),
                annot = True,
                cbar= False,
                fmt='g')
plt.xlabel('Predicted value')
plt.ylabel('Actual value')
plt.title('Confusion Matrix')

from sklearn.metrics import recall_score
from sklearn.metrics import accuracy_score, precision_score, f1_score

print('recall score: ', recall_score(y_train, y_hat, average='macro'))

print('accuracy_score', accuracy_score(y_train,y_hat))

print('precision_score', precision_score(y_train,y_hat, average='macro'))

print('f1_score', f1_score(y_train,y_hat, average='macro'))

len(y_hat)

# y_hat = 예측
# y_train = 정답
# yhat/y_train
ZZ = 0
ZO = 0
ZTw = 0
ZTh = 0
OZ = 0
OO = 0
OTw = 0
OTh = 0
TwZ =0
TwO = 0
TwTw = 0
TwTh = 0
ThZ = 0
ThO = 0
ThTw = 0
ThTh = 0

for i in range (len(y_hat)):
  #print('y_hat: ',y_hat[i])
  #print('y_train: ',y_train[i])
  if (y_hat[i] == 0 and y_train[i]== 0):
    ZZ = ZZ+1 # 정답
    #print('ZZ')
  elif (y_hat[i] == 0 and y_train[i]== 1):
    ZO = ZO +1 # 오답 
    #print('ZO')
  elif (y_hat[i] == 0 and y_train[i]== 2):
    ZTw = ZTw +1 # 오답
    #print('ZTw')
  elif (y_hat[i] == 0 and y_train[i]== 3):
    ZTh = ZTh +1 #오답
    #print('ZTh')
  elif (y_hat[i] == 1 and y_train[i]== 0):
    OZ = OZ +1 # 오답 but ㄱㅊ
    #print('OZ')
  elif (y_hat[i] == 1 and y_train[i]== 1):
    OO = OO +1 # 정답
    #print('OO')
  elif (y_hat[i] == 1 and y_train[i]== 2):
    OTw = OTw +1 #오답
    #print('OTw')
  elif (y_hat[i] == 1 and y_train[i]== 3):
    OTh = OTh +1 #오답
    #print('OTh')
  elif (y_hat[i] == 2 and y_train[i]== 0):
    TwZ = TwZ +1 #오답 but ㄱㅊ
    #print('TwZ')
  elif (y_hat[i] == 2 and y_train[i]== 1):
    TwO = TwO +1  # 오답 but ㄱㅊ
    #print('TwO')
  elif (y_hat[i] == 2 and y_train[i]== 2):
    TwTw = TwTw +1 # 정답
    #print('TwTw')
  elif (y_hat[i] == 2 and y_train[i]== 3):
    TwTh = TwTh +1 #오답
    #print('TwTh')
  elif (y_hat[i] == 3 and y_train[i]== 0):
    ThZ = ThZ +1 # 오답 but ㄱㅊ
    #print('ThZ')
  elif (y_hat[i] == 3 and y_train[i]== 1):
    ThO = ThO +1 #오답 but ㄱㅊ
    #print('ThO')
  elif (y_hat[i] == 3 and y_train[i]== 2):
    ThTw = ThTw +1 # 오답 but ㄱㅊ
    #print('ThTw')
  elif (y_hat[i] == 3 and y_train[i]== 3):
    ThTh = ThTh +1 # 정답
    #print('ThTh')

#print(len(y_hat))
print('ZZ :',ZZ)
print('ZO :',ZO)
print('ZTw :',ZTw)
print('ZTh :',ZTh)
print('OZ :',OZ)
print('OO :',OO)
print('OTw :',OTw)
print('OTh :',OTh)
print('TwZ :',TwZ)
print('TwO :',TwO)
print('TwTw :',TwTw)
print('TwTh :',TwTh)
print('ThZ :',ThZ)
print('ThO :',ThO)
print('ThTw :',ThTw)
print('ThTh :',ThTh)

total = ZZ+ZO+ZTw+ZTh+OZ+OO+OTw+OTh+TwZ+TwO+TwTw+TwTh+ThZ+ThO+ThTw+ThTh
correct = ZZ+ OO+ TwTw+ThTh
wrong = total - correct
notbad = OZ+ TwZ+TwO+ ThZ + ThO+ ThTw
print('전체 갯수는: ',ZZ+ZO+ZTw+ZTh+OZ+OO+OTw+OTh+TwZ+TwO+TwTw+TwTh+ThZ+ThO+ThTw+ThTh)
print('정답 갯수는: ', ZZ+ OO+ TwTw+ThTh)
print('오답 갯수는: ', wrong)
print('오답 이지만 괜찮은 갯수는: ',notbad)
print('틀리면 안됐던 값: ', (wrong-notbad)/total * 100 , '%')
print('맞춘 갯수(틀려도 괜찮은 수치 포함):' , (correct+notbad)/total * 100,'%')






