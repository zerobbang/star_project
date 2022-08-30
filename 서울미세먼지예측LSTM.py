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
# 데이터 시각화
import matplotlib
import matplotlib.pyplot as plt 
# pip install seaborn
# pip install matplotlib
# pip install tensorflow
import seaborn as sns 
print("matplotlib version:", matplotlib.__version__)
print("seaborn version:", sns.__version__)

# 모델
from pickle import dump
from sklearn.metrics import accuracy_score
import math
import keras
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM
from keras.layers import Dropout
from keras.layers import *
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_squared_error
from sklearn.metrics import mean_absolute_error
from sklearn.model_selection import train_test_split
from keras.callbacks import EarlyStopping
from sklearn.metrics import confusion_matrix

dir = os.path.dirname(os.path.realpath(__file__))

seoul2019_nohumadity = pd.read_csv(dir+"/need_files/2019기상_습도없는구.csv", encoding='cp949')[['지점명','일시','풍향(deg)','풍속(m/s)','강수량(mm)']]
seoul2019_humadity = pd.read_csv(dir+"/need_files/2019기상_습도있는구.csv", encoding='cp949')
seoul2019_mise_no = pd.read_csv(dir+"/need_files/2019서울미세먼지_nohumadity.csv", encoding='cp949')
seoul2019_mise_yes = pd.read_csv(dir+"/need_files/2019서울미세먼지_humadity.csv", encoding='cp949')
sandong_2019_before=pd.read_csv(dir+"/need_files/china_2019_sangdong_before.csv", encoding='cp949')

seoul2019_mise_no.rename(columns = {'측정소명':'지점명', '측정일시':'일시'},inplace=True)
seoul2019_nohumadity.rename(columns = {'풍향(deg)':'풍향(16방위)'}, inplace=True)
seoul2019_mise_yes.rename(columns = {'측정소명':'지점명', '측정일시':'일시'},inplace=True)
seoul2019_humadity.rename(columns = {'풍향(deg)':'풍향(16방위)'}, inplace=True)
sandong_2019_before = sandong_2019_before.drop('Unnamed: 0', axis=1)
sandong_2019_before.rename(columns = {'mean':'중국 1시간평균 미세먼지농도(㎍/㎥)'},inplace=True)
sandong_2019_before['중국 1시간평균 미세먼지농도(㎍/㎥)'] = sandong_2019_before['중국 1시간평균 미세먼지농도(㎍/㎥)'].astype(int)



seoul2019_nohumadity['일시'] = pd.to_datetime(seoul2019_nohumadity['일시'], format = '%Y-%m-%d %H:%M') # 서울 기상, 습도없는 구들
seoul2019_humadity['일시'] = pd.to_datetime(seoul2019_humadity['일시'], format = '%Y-%m-%d %H:%M') # 서울 기상, 습도 있는 구들
seoul2019_mise_no['일시'] = pd.to_datetime(seoul2019_mise_no['일시'], format =  '%Y-%m-%d %H:%M') # 서울 미세먼지, 습도 없는 구들
seoul2019_mise_yes['일시'] = pd.to_datetime(seoul2019_mise_yes['일시'], format =  '%Y-%m-%d %H:%M') #서울 미세먼지, 습도 있는 구들
seoul2019_no = pd.merge(seoul2019_mise_no ,seoul2019_nohumadity, "left", on=["일시","지점명"]) # 습도 없는 구들 합침
seoul2019_yes = pd.merge(seoul2019_mise_yes, seoul2019_humadity, "left", on=["일시","지점명"]) # 습도 있는 구들 합침
seoul2019_no['date'] = seoul2019_no['일시'].dt.strftime('%Y-%m-%d')
seoul2019_no['date'] = pd.to_datetime(seoul2019_no['date'], format = '%Y-%m-%d')
seoul2019_yes['date'] = seoul2019_yes['일시'].dt.strftime('%Y-%m-%d')
seoul2019_yes['date'] = pd.to_datetime(seoul2019_yes['date'], format = '%Y-%m-%d')
sandong_2019_before['date'] = pd.to_datetime(sandong_2019_before['date'], format = '%Y-%m-%d %H:%M')

seoul2019_no = pd.merge(seoul2019_no, sandong_2019_before, 'left', on='date')
seoul2019_no = seoul2019_no.drop('date', axis=1)
seoul2019_yes = pd.merge(seoul2019_yes, sandong_2019_before, 'left', on='date')
seoul2019_yes = seoul2019_yes.drop('date', axis=1)

seoul2019_yes = seoul2019_yes.sort_values(by=[seoul2019_yes.columns[1],seoul2019_yes.columns[0]],ascending=True, ignore_index=True)
seoul2019_no = seoul2019_no.sort_values(by=[seoul2019_no.columns[1],seoul2019_no.columns[0]],ascending=True, ignore_index=True)
seoul2019_yes[seoul2019_yes['지점명']=='마포구']

# 강수량 전처리 , null 값을 0으로
seoul2019_no['강수량(mm)'] = seoul2019_no['강수량(mm)'].fillna(0)
seoul2019_yes['강수량(mm)'] = seoul2019_yes['강수량(mm)'].fillna(0)

seoul2019_yes.isnull().sum()

seoul2019_no.isnull().sum()

# 풍향 결측치 처리

# 평균값으로 가기 보다는 흐름이 있기 때문에 이전 값을 가져오는 형식으로 진행

#se20['풍향(16방위)'] = se20['풍향(16방위)'].fillna(se20['풍향(16방위)'].interpolate())
seoul2019_no['풍향(16방위)'] = seoul2019_no['풍향(16방위)'].fillna(seoul2019_no['풍향(16방위)'].interpolate())
seoul2019_yes['풍향(16방위)'] = seoul2019_yes['풍향(16방위)'].fillna(seoul2019_yes['풍향(16방위)'].interpolate())

# 풍향 카테고리화
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 0.0) & (seoul2019_no['풍향(16방위)'] < 11.25) , '풍향D'] = 0
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 11.25) & (seoul2019_no['풍향(16방위)'] < 33.75) , '풍향D'] = 22.5
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 33.75) & (seoul2019_no['풍향(16방위)'] < 56.25) , '풍향D'] = 45
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 56.25) & (seoul2019_no['풍향(16방위)'] < 78.75) , '풍향D'] = 67.5
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 78.75) & (seoul2019_no['풍향(16방위)'] < 101.25) , '풍향D'] = 90
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 101.25) & (seoul2019_no['풍향(16방위)'] < 123.75) , '풍향D'] = 112.5
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 123.75) & (seoul2019_no['풍향(16방위)'] < 146.25) , '풍향D'] = 135
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 146.25) & (seoul2019_no['풍향(16방위)'] < 168.75) , '풍향D'] = 157.5
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 168.75) & (seoul2019_no['풍향(16방위)'] < 191.25) , '풍향D'] = 180
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 191.25) & (seoul2019_no['풍향(16방위)'] < 213.75) , '풍향D'] = 202.5
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 213.75) & (seoul2019_no['풍향(16방위)'] < 236.25) , '풍향D'] = 225
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 236.25) & (seoul2019_no['풍향(16방위)'] < 258.75) , '풍향D'] = 247.5
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 258.75) & (seoul2019_no['풍향(16방위)'] < 281.25) , '풍향D'] = 270
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 281.25) & (seoul2019_no['풍향(16방위)'] < 303.75) , '풍향D'] = 292.5
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 303.75) & (seoul2019_no['풍향(16방위)'] < 326.25) , '풍향D'] = 315
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >= 326.25) & (seoul2019_no['풍향(16방위)'] < 348.75) , '풍향D'] = 337.5
seoul2019_no.loc[(seoul2019_no['풍향(16방위)'] >=348.75) & (seoul2019_no['풍향(16방위)'] <= 360) , '풍향D'] = 0

seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 0.0) & (seoul2019_yes['풍향(16방위)'] < 11.25) , '풍향D'] = 0
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 11.25) & (seoul2019_yes['풍향(16방위)'] < 33.75) , '풍향D'] = 22.5
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 33.75) & (seoul2019_yes['풍향(16방위)'] < 56.25) , '풍향D'] = 45
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 56.25) & (seoul2019_yes['풍향(16방위)'] < 78.75) , '풍향D'] = 67.5
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 78.75) & (seoul2019_yes['풍향(16방위)'] < 101.25) , '풍향D'] = 90
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 101.25) & (seoul2019_yes['풍향(16방위)'] < 123.75) , '풍향D'] = 112.5
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 123.75) & (seoul2019_yes['풍향(16방위)'] < 146.25) , '풍향D'] = 135
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 146.25) & (seoul2019_yes['풍향(16방위)'] < 168.75) , '풍향D'] = 157.5
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 168.75) & (seoul2019_yes['풍향(16방위)'] < 191.25) , '풍향D'] = 180
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 191.25) & (seoul2019_yes['풍향(16방위)'] < 213.75) , '풍향D'] = 202.5
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 213.75) & (seoul2019_yes['풍향(16방위)'] < 236.25) , '풍향D'] = 225
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 236.25) & (seoul2019_yes['풍향(16방위)'] < 258.75) , '풍향D'] = 247.5
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 258.75) & (seoul2019_yes['풍향(16방위)'] < 281.25) , '풍향D'] = 270
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 281.25) & (seoul2019_yes['풍향(16방위)'] < 303.75) , '풍향D'] = 292.5
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 303.75) & (seoul2019_yes['풍향(16방위)'] < 326.25) , '풍향D'] = 315
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >= 326.25) & (seoul2019_yes['풍향(16방위)'] < 348.75) , '풍향D'] = 337.5
seoul2019_yes.loc[(seoul2019_yes['풍향(16방위)'] >=348.75) & (seoul2019_yes['풍향(16방위)'] <= 360) , '풍향D'] = 0

seoul2019_yes

## 시간별 풍속 데이터 결측치 보정

## 만약에 첫번째 값이 결측치 인 경우, 가장 최초로 나오는 결측치 가 아닌 값을 넣는다.
a = 1
while(True):
  if np.isnan(seoul2019_yes['풍속(m/s)'][0]):
    seoul2019_yes['풍속(m/s)'][0] = seoul2019_yes['풍속(m/s)'][a]
    a = a+1
  else:
    break

## 첫번째 이후의 결측치를 보정
## 실측/결측/실측 의 경우 양쪽 실측값들 사이의 평균을 결측치로 보정함
## 실측/ 결측/ 결측의 경우 앞의 실측값을 그대로 불러와서 보정함
## 맨 마지막 값이 결측치 인 경우 그 직전값을 불러와서 보정함.
for i in range(len(seoul2019_yes['풍속(m/s)'])):
  i = i+1
  if i == len(seoul2019_yes['풍속(m/s)'])-1:
    if np.isnan(seoul2019_yes['풍속(m/s)'][i]):
      seoul2019_yes['풍속(m/s)'][i] = seoul2019_yes['풍속(m/s)'][i-1]
    break
  elif np.isnan(seoul2019_yes['풍속(m/s)'][i]):
    if np.isnan(seoul2019_yes['풍속(m/s)'][i+1]):
      seoul2019_yes['풍속(m/s)'][i] = seoul2019_yes['풍속(m/s)'][i-1]
    else:
      seoul2019_yes['풍속(m/s)'][i] = (seoul2019_yes['풍속(m/s)'][i-1]+seoul2019_yes['풍속(m/s)'][i+1])/2

## 시간별 풍속 데이터 결측치 보정

## 만약에 첫번째 값이 결측치 인 경우, 가장 최초로 나오는 결측치 가 아닌 값을 넣는다.
a = 1
while(True):
  if np.isnan(seoul2019_no['풍속(m/s)'][0]):
    seoul2019_no['풍속(m/s)'][0] = seoul2019_no['풍속(m/s)'][a]
    a = a+1
  else:
    break

## 첫번째 이후의 결측치를 보정
## 실측/결측/실측 의 경우 양쪽 실측값들 사이의 평균을 결측치로 보정함
## 실측/ 결측/ 결측의 경우 앞의 실측값을 그대로 불러와서 보정함
## 맨 마지막 값이 결측치 인 경우 그 직전값을 불러와서 보정함.
for i in range(len(seoul2019_no['풍속(m/s)'])):
  i = i+1
  if i == len(seoul2019_no['풍속(m/s)'])-1:
    if np.isnan(seoul2019_no['풍속(m/s)'][i]):
      seoul2019_no['풍속(m/s)'][i] = seoul2019_no['풍속(m/s)'][i-1]
    break
  elif np.isnan(seoul2019_no['풍속(m/s)'][i]):
    if np.isnan(seoul2019_no['풍속(m/s)'][i+1]):
      seoul2019_no['풍속(m/s)'][i] = seoul2019_no['풍속(m/s)'][i-1]
    else:
      seoul2019_no['풍속(m/s)'][i] = (seoul2019_no['풍속(m/s)'][i-1]+seoul2019_no['풍속(m/s)'][i+1])/2

## 시간별 습도 데이터 결측치 보정(습도 있는 yes만 진행)

## 만약에 첫번째 값이 결측치 인 경우, 가장 최초로 나오는 결측치 가 아닌 값을 넣는다.
a = 1
while(True):
  if np.isnan(seoul2019_yes['습도(%)'][0]):
    seoul2019_yes['습도(%)'][0] = seoul2019_yes['습도(%)'][a]
    a = a+1
  else:
    break

## 첫번째 이후의 결측치를 보정
## 실측/결측/실측 의 경우 양쪽 실측값들 사이의 평균을 결측치로 보정함
## 실측/ 결측/ 결측의 경우 앞의 실측값을 그대로 불러와서 보정함
## 맨 마지막 값이 결측치 인 경우 그 직전값을 불러와서 보정함.
for i in range(len(seoul2019_yes['습도(%)'])):
  i = i+1
  if i == len(seoul2019_yes['습도(%)'])-1:
    if np.isnan(seoul2019_yes['습도(%)'][i]):
      seoul2019_yes['습도(%)'][i] = seoul2019_yes['습도(%)'][i-1]
    break
  elif np.isnan(seoul2019_yes['습도(%)'][i]):
    if np.isnan(seoul2019_yes['습도(%)'][i+1]):
      seoul2019_yes['습도(%)'][i] = seoul2019_yes['습도(%)'][i-1]
    else:
      seoul2019_yes['습도(%)'][i] = (seoul2019_yes['습도(%)'][i-1]+seoul2019_yes['습도(%)'][i+1])/2



## 시간별 미세먼지 농도 데이터 결측치 보정



mask = seoul2019_yes['미세먼지 1시간(㎍/㎥)'].isin([0])
seoul2019_yes= seoul2019_yes[~mask]

## 시간별 미세먼지 농도 데이터 결측치 보정



mask = seoul2019_no['미세먼지 1시간(㎍/㎥)'].isin([0])
seoul2019_no= seoul2019_no[~mask]

seoul2019_no[seoul2019_no['지점명']== '서초구']

# 강남구에서만 일단 테스트 진행
df = seoul2019_yes[seoul2019_yes['지점명'] == '종로구']

ydata = df[['풍속(m/s)','풍향D','강수량(mm)','습도(%)','중국 1시간평균 미세먼지농도(㎍/㎥)', '미세먼지 1시간(㎍/㎥)']]
ydata.index = df['일시']
#print(ydata)
# MinMax표준화
transformer = MinMaxScaler()
transformer2 = MinMaxScaler()
transformer2.fit_transform(ydata[['미세먼지 1시간(㎍/㎥)']])
ydata=transformer.fit_transform(ydata)


# # scaler 저장
# from pickle import dump
# dump(transformer, open('/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/동대문구/동대문구minmax_scaler_x.pkl','wb'))
# dump(transformer2, open('/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/동대문구/동대문구minmax_scaler_y.pkl','wb'))

# scaler 불러오기:
# from pickle import load
# load_minmax_scaler = load(open('/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/동대문구/동대문구minmax_scaler.pkl','rb'))

"""# FUTURE 값 수정하는 곳"""

"""# 한스텝 예측"""
# future_target = 4 # 앞으로의 n시간뒤 데이터

# def multivariate_data(dataset, target, start_index, end_index, history_size, target_size, step, single_step=False):
#     data = []
#     labels = []

#     start_index = start_index + history_size
#     if end_index is None:
#         end_index = len(dataset) - target_size

#     for i in range(start_index, end_index):
#         indices = range(i - history_size, i, step)
#         data.append(dataset[indices])

#         if single_step:
#             labels.append(target[i + target_size])
#         else:
#             labels.append(target[i:i + target_size])
#     return np.array(data), np.array(labels)


# past_history = 72 # 지난 24시간의 데이터

# STEP = 1
# #TRAIN_SPLIT = int(len(df)*0.9)
# TRAIN_SPLIT = int(len(df) * 0.7)
# tf.random.set_seed(13)
# x_train_single, y_train_single = multivariate_data(ydata, ydata[:, 5], 0, TRAIN_SPLIT, past_history,
#                                                    future_target, STEP
#                                                    , single_step=True)
# x_val_single, y_val_single = multivariate_data(ydata, ydata[:, 5], TRAIN_SPLIT, None, past_history,
#                                                future_target, STEP
#                                                     , single_step=True)
# #print('y_val_single:', y_val_single)
# #print('ydata[:,5]', ydata[:,5])

# print('Single window of past history : {}'.format(x_train_single[0].shape))
# #print('\n Target mise to predict : {}'.format(y_train_single[0].shape))
# #print('x_val_single: ', x_val_single, x_val_single.shape())
# #print('y_val_single: ', y_val_single, y_val_single.shape())
# real_answer = []
# predicted_answer=[]
# def create_time_steps(length):
#     return list(range(-length, 0))
# def show_plot(plot_data, delta, title):
#     labels = ['History', 'True Future', 'Model Prediction']
#     marker = ['.-', 'rx', 'go']
#     time_steps = create_time_steps(plot_data[0].shape[0])
#     if delta:
#         future = delta
#     else:
#         future = 0

#     plt.title(title)
#     for i, x in enumerate(plot_data):
#         if i:
#             plt.plot(future, transformer2.inverse_transform(plot_data[i]), marker[i], markersize=10, label=labels[i])
#             # i == i : 실제값, i ==2 : 예측값
#             if i == 1:
#               real_answer.append(transformer2.inverse_transform(plot_data[i])[0][0])
#             elif i == 2:
#               predicted_answer.append(transformer2.inverse_transform(plot_data[i])[0][0])#[0])
#             #print('여기인가?',transformer2.inverse_transform(plot_data[i]))
#         else:
#             plt.plot(time_steps, transformer2.inverse_transform(plot_data[i]).flatten(), marker[i], label=labels[i])
#             #print('이전의흐름: ',transformer2.inverse_transform(plot_data[i]).flatten())
#     plt.legend()
#     plt.axis('auto')
#     plt.xlim([time_steps[0], (future+5)*2])
#     plt.xlabel('Time-Step')
    
#     return plt
# def baseline(history):
#     return np.mean(history)

# print(x_train_single.shape)

# BATCH_SIZE = 72 # 1step에서 사용되는 데이터 수
# BUFFER_SIZE = 3500 # 1epoch 되는 데이터 수
# EVALUATION_INTERVAL = 80
# EPOCHS = 40
# train_data_single = tf.data.Dataset.from_tensor_slices((x_train_single, y_train_single))
# train_data_single = train_data_single.cache().shuffle(BUFFER_SIZE).batch(BATCH_SIZE).repeat()

# val_data_single = tf.data.Dataset.from_tensor_slices((x_val_single, y_val_single))
# val_data_single = val_data_single.batch(BATCH_SIZE).repeat()

# single_step_model = tf.keras.models.Sequential()
# single_step_model.add(tf.keras.layers.LSTM(32, input_shape=x_train_single.shape[-2:]))
# single_step_model.add(tf.keras.layers.Dense(1))
# single_step_model.compile(optimizer='adam', loss='mae') # tf.keras.optimizers.RMSprop()
# early_stopping_monitor = EarlyStopping(monitor='val_loss', min_delta=0, patience = 8, verbose=1, mode='auto', baseline=None, restore_best_weights=True)

# # optimizers adam?
# for x, y in val_data_single.take(1):
#     print(single_step_model.predict(x).shape)

# single_step_history = single_step_model.fit(train_data_single, epochs=EPOCHS,
#                                             steps_per_epoch=EVALUATION_INTERVAL,
#                                             validation_data=val_data_single,
#                                             validation_steps=140,
#                                             callbacks=[early_stopping_monitor])


# def plot_train_history(history, title):
#     loss = history.history['loss']
#     val_loss = history.history['val_loss']

#     epochs = range(len(loss))

#     plt.figure()

#     plt.plot(epochs, loss, 'b', label='Training loss')
#     plt.plot(epochs, val_loss, 'r', label='Validation loss')
#     plt.title(title)
#     plt.legend()

#     plt.show()

# #plot_train_history(single_step_history,
# #                   'Single Step Training and Validation Loss')

# #print(single_step_model.predict(x)[0]) # 예측하는 정답
# #print(x[0][:,5]) # 이전 값
# #print(y[0]) # 진짜 정답

# print(single_step_model.predict(x))
# print(x[0][:,5])
# print(y[0])
# #한스텝 예측 진행
# for x, y in val_data_single.take(10):
 
#  plot = show_plot([x[0][:, 5].numpy().reshape(-1,1), y[0].numpy().reshape(-1,1),
#                      single_step_model.predict(x)[0].reshape(-1,1)], 4,
#                     'Single Step Prediction')
#  plot.show()

# real_answer = []
# predicted_answer = []
# for x, y in val_data_single.take(10):
#   plot_datas2 = [x[0][:, 5].numpy().reshape(-1,1), y[0].numpy().reshape(-1,1),
#                       single_step_model.predict(x)[0].reshape(-1,1)]
#   for i, x in enumerate(plot_datas2):
#         if i:
            
#             # i == i : 실제값, i ==2 : 예측값
#             if i == 1:
#               real_answer.append(transformer2.inverse_transform(plot_datas2[i])[0][0])
#               print('real_answer:',real_answer)
#             elif i == 2:
#               predicted_answer.append(transformer2.inverse_transform(plot_datas2[i])[0][0])#[0])
#               print('predicted::',predicted_answer)
#             #print('여기인가?',transformer2.inverse_transform(plot_datas2[i]))
#         else:
            
#             print('이전의흐름: ',transformer2.inverse_transform(plot_datas2[i]).flatten())

# real_answer = list(map(int,real_answer))
# predicted_answer = list(map(int,predicted_answer))

# for i in range(len(real_answer)):
#   if real_answer[i] <= 30:
#     real_answer[i] = 0
#   elif real_answer[i] >30 and real_answer[i] <=80 :
#     real_answer[i] = 1
#   elif real_answer[i] >80 and real_answer[i] <=150:
#     real_answer[i] = 2
#   elif real_answer[i] > 150:
#     real_answer[i] = 3
# for i in range(len(predicted_answer)):
#   if predicted_answer[i] <= 30:
#     predicted_answer[i] = 0
#   elif predicted_answer[i] >30 and predicted_answer[i] <=80 :
#     predicted_answer[i] = 1
#   elif predicted_answer[i] >80 and predicted_answer[i] <=150:
#     predicted_answer[i] = 2
#   elif predicted_answer[i] > 150:
#     predicted_answer[i] = 3

# # print(real_answer)
# # print(predicted_answer)

# sns.heatmap(confusion_matrix(real_answer,predicted_answer),
#                 annot = True,
#                 cbar= False,
#                 fmt='g')
# plt.xlabel('Predicted value')
# plt.ylabel('Actual value')
# plt.title('Confusion Matrix')

# a = [8.0, 24.999999999999996, 4.0, 23.0, 20.0, 49.0, 22.0, 9.999999999999998, 28.999999999999996, 16.0]
# b = [11.344651, 26.904247, 11.495723, 24.983341, 25.772505, 46.364624, 15.633798, 7.5917435, 24.653858, 16.621576]
# d = 0
# for i in range(len(a)):
#   c = (a[i]-b[i])**2
#   d = d+c
#   print(d)
# print('최종 d는 말이죵:', (d/10)**(1/2))



"""# 머신 러닝 저 장"""

#머신러닝 저장
# 구역별로, 필요한 시간 별로 저장해야한다.

#single_step_model.save('/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/동대문구/동대문구+72.h5')





# 머신러닝 저장

# 강남구에서만 일단 테스트 진행
#locations = ['강남구','강동구','강북구','광진구','구로구','노원구','도봉구','동작구','마포구','성동구','성북구','종로구','중랑구' ] # + 동대문구
locations = ['도봉구','동작구','마포구','성동구','성북구','종로구','중랑구' ] # + 동대문구
for location in locations:
  futures = [4,8,12,16,20,24,48,72]
  df = seoul2019_yes[seoul2019_yes['지점명'] == location]
  ydata = df[['풍속(m/s)','풍향D','강수량(mm)','습도(%)','중국 1시간평균 미세먼지농도(㎍/㎥)', '미세먼지 1시간(㎍/㎥)']]
  ydata.index = df['일시']
  #print(ydata)
  # MinMax표준화
  transformer = MinMaxScaler()
  transformer2 = MinMaxScaler()
  transformer2.fit_transform(ydata[['미세먼지 1시간(㎍/㎥)']])
  ydata=transformer.fit_transform(ydata)
  os.makedirs('/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/' + location, exist_ok=True)
  filename1 = '/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/' + location +'/'+location+'minmax_scaler_x.pkl'
  filename2 = '/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/' + location +'/'+location+'minmax_scaler_y.pkl'
  dump(transformer, open(filename1,'wb'))
  dump(transformer2, open(filename2,'wb'))


  # ydata=transformer.fit_transform(ydata)
  #print((ydata.shape))
  #print(type(ydata))

  # 한스텝 예측

  for future_num in futures:
    future_target = future_num # 앞으로의 n시간뒤 데이터


    def multivariate_data(dataset, target, start_index, end_index, history_size, target_size, step, single_step=False):
        data = []
        labels = []

        start_index = start_index + history_size
        if end_index is None:
            end_index = len(dataset) - target_size

        for i in range(start_index, end_index):
            indices = range(i - history_size, i, step)
            data.append(dataset[indices])

            if single_step:
                labels.append(target[i + target_size])
            else:
                labels.append(target[i:i + target_size])
        return np.array(data), np.array(labels)


    past_history = 72 # 지난 24시간의 데이터

    STEP = 1
    TRAIN_SPLIT = int(len(df)*0.9)
    #TRAIN_SPLIT = int(len(df) * 0.7)
    tf.random.set_seed(13)
    x_train_single, y_train_single = multivariate_data(ydata, ydata[:, 5], 0, TRAIN_SPLIT, past_history,
                                                      future_target, STEP
                                                      , single_step=True)
    x_val_single, y_val_single = multivariate_data(ydata, ydata[:, 5], TRAIN_SPLIT, None, past_history,
                                                  future_target, STEP
                                                        , single_step=True)
    #print('y_val_single:', y_val_single)
    #print('ydata[:,5]', ydata[:,5])

    print('Single window of past history : {}'.format(x_train_single[0].shape))
    #print('\n Target mise to predict : {}'.format(y_train_single[0].shape))
    #print('x_val_single: ', x_val_single, x_val_single.shape())
    #print('y_val_single: ', y_val_single, y_val_single.shape())
    real_answer = []
    predicted_answer=[]
    def create_time_steps(length):
        return list(range(-length, 0))
    def show_plot(plot_data, delta, title):
        labels = ['History', 'True Future', 'Model Prediction']
        marker = ['.-', 'rx', 'go']
        time_steps = create_time_steps(plot_data[0].shape[0])
        if delta:
            future = delta
        else:
            future = 0

        plt.title(title)
        for i, x in enumerate(plot_data):
            if i:
                plt.plot(future, transformer2.inverse_transform(plot_data[i]), marker[i], markersize=10, label=labels[i])
                # i == i : 실제값, i ==2 : 예측값
                if i == 1:
                  real_answer.append(transformer2.inverse_transform(plot_data[i])[0][0])
                elif i == 2:
                  predicted_answer.append(transformer2.inverse_transform(plot_data[i])[0][0])#[0])
                #print('여기인가?',transformer2.inverse_transform(plot_data[i]))
            else:
                plt.plot(time_steps, transformer2.inverse_transform(plot_data[i]).flatten(), marker[i], label=labels[i])
                #print('이전의흐름: ',transformer2.inverse_transform(plot_data[i]).flatten())
        plt.legend()
        plt.axis('auto')
        plt.xlim([time_steps[0], (future+5)*2])
        plt.xlabel('Time-Step')
        
        return plt
    def baseline(history):
        return np.mean(history)


    BATCH_SIZE = 72 # 1step에서 사용되는 데이터 수
    BUFFER_SIZE = 3500 # 1epoch 되는 데이터 수
    EVALUATION_INTERVAL = 80
    EPOCHS = 40
    train_data_single = tf.data.Dataset.from_tensor_slices((x_train_single, y_train_single))
    train_data_single = train_data_single.cache().shuffle(BUFFER_SIZE).batch(BATCH_SIZE).repeat()

    val_data_single = tf.data.Dataset.from_tensor_slices((x_val_single, y_val_single))
    val_data_single = val_data_single.batch(BATCH_SIZE).repeat()

    single_step_model = tf.keras.models.Sequential()
    single_step_model.add(tf.keras.layers.LSTM(32, input_shape=x_train_single.shape[-2:]))
    single_step_model.add(tf.keras.layers.Dense(1))
    single_step_model.compile(optimizer='adam', loss='mae') # tf.keras.optimizers.RMSprop()
    early_stopping_monitor = EarlyStopping(monitor='val_loss', min_delta=0, patience = 8, verbose=1, mode='auto', baseline=None, restore_best_weights=True)

    # optimizers adam?
    for x, y in val_data_single.take(1):
        print(single_step_model.predict(x).shape)

    single_step_history = single_step_model.fit(train_data_single, epochs=EPOCHS,
                                                steps_per_epoch=EVALUATION_INTERVAL,
                                                validation_data=val_data_single,
                                                validation_steps=140,
                                                callbacks=[early_stopping_monitor])


    def plot_train_history(history, title):
        loss = history.history['loss']
        val_loss = history.history['val_loss']

        epochs = range(len(loss))

        plt.figure()

        plt.plot(epochs, loss, 'b', label='Training loss')
        plt.plot(epochs, val_loss, 'r', label='Validation loss')
        plt.title(title)
        plt.legend()

        plt.show()

    #plot_train_history(single_step_history,
    #                   'Single Step Training and Validation Loss')


    #머신러닝 저장
    # 구역별로, 필요한 시간 별로 저장해야한다.
    modelfilename = '/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/'+ location+'/'+location+str(future_num)+'.h5'
    single_step_model.save(modelfilename)

# 습도가 없는 지역들
locations = ['서초구','송파구','강서구','양천구','서대문구','용산구','은평구','금천구','중구','관악구','영등포구', ] 

for location in locations:
  futures = [4,8,12,16,20,24,48,72]
  df = seoul2019_no[seoul2019_no['지점명'] == location]
  ydata = df[['풍속(m/s)','풍향D','강수량(mm)','중국 1시간평균 미세먼지농도(㎍/㎥)', '미세먼지 1시간(㎍/㎥)']]
  ydata.index = df['일시']
  #print(ydata)
  # MinMax표준화
  transformer = MinMaxScaler()
  transformer2 = MinMaxScaler()
  transformer2.fit_transform(ydata[['미세먼지 1시간(㎍/㎥)']])
  ydata=transformer.fit_transform(ydata)
  os.makedirs('/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/' + location, exist_ok=True)
  filename1 = '/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/' + location +'/'+location+'minmax_scaler_x.pkl'
  filename2 = '/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/' + location +'/'+location+'minmax_scaler_y.pkl'
  dump(transformer, open(filename1,'wb'))
  dump(transformer2, open(filename2,'wb'))


  # ydata=transformer.fit_transform(ydata)
  #print((ydata.shape))
  #print(type(ydata))

  # 한스텝 예측

  for future_num in futures:
    future_target = future_num # 앞으로의 n시간뒤 데이터


    def multivariate_data(dataset, target, start_index, end_index, history_size, target_size, step, single_step=False):
        data = []
        labels = []

        start_index = start_index + history_size
        if end_index is None:
            end_index = len(dataset) - target_size

        for i in range(start_index, end_index):
            indices = range(i - history_size, i, step)
            data.append(dataset[indices])

            if single_step:
                labels.append(target[i + target_size])
            else:
                labels.append(target[i:i + target_size])
        return np.array(data), np.array(labels)


    past_history = 72 # 지난 24시간의 데이터

    STEP = 1
    TRAIN_SPLIT = int(len(df)*0.9)
    #TRAIN_SPLIT = int(len(df) * 0.7)
    tf.random.set_seed(13)
    x_train_single, y_train_single = multivariate_data(ydata, ydata[:, 4], 0, TRAIN_SPLIT, past_history,
                                                      future_target, STEP
                                                      , single_step=True)
    x_val_single, y_val_single = multivariate_data(ydata, ydata[:, 4], TRAIN_SPLIT, None, past_history,
                                                  future_target, STEP
                                                        , single_step=True)
    #print('y_val_single:', y_val_single)
    #print('ydata[:,5]', ydata[:,5])

    print('Single window of past history : {}'.format(x_train_single[0].shape))
    #print('\n Target mise to predict : {}'.format(y_train_single[0].shape))
    #print('x_val_single: ', x_val_single, x_val_single.shape())
    #print('y_val_single: ', y_val_single, y_val_single.shape())
    real_answer = []
    predicted_answer=[]
    def create_time_steps(length):
        return list(range(-length, 0))
    def show_plot(plot_data, delta, title):
        labels = ['History', 'True Future', 'Model Prediction']
        marker = ['.-', 'rx', 'go']
        time_steps = create_time_steps(plot_data[0].shape[0])
        if delta:
            future = delta
        else:
            future = 0

        plt.title(title)
        for i, x in enumerate(plot_data):
            if i:
                plt.plot(future, transformer2.inverse_transform(plot_data[i]), marker[i], markersize=10, label=labels[i])
                # i == i : 실제값, i ==2 : 예측값
                if i == 1:
                  real_answer.append(transformer2.inverse_transform(plot_data[i])[0][0])
                elif i == 2:
                  predicted_answer.append(transformer2.inverse_transform(plot_data[i])[0][0])#[0])
                #print('여기인가?',transformer2.inverse_transform(plot_data[i]))
            else:
                plt.plot(time_steps, transformer2.inverse_transform(plot_data[i]).flatten(), marker[i], label=labels[i])
                #print('이전의흐름: ',transformer2.inverse_transform(plot_data[i]).flatten())
        plt.legend()
        plt.axis('auto')
        plt.xlim([time_steps[0], (future+5)*2])
        plt.xlabel('Time-Step')
        
        return plt
    def baseline(history):
        return np.mean(history)


    BATCH_SIZE = 72 # 1step에서 사용되는 데이터 수
    BUFFER_SIZE = 3500 # 1epoch 되는 데이터 수
    EVALUATION_INTERVAL = 80
    EPOCHS = 40
    train_data_single = tf.data.Dataset.from_tensor_slices((x_train_single, y_train_single))
    train_data_single = train_data_single.cache().shuffle(BUFFER_SIZE).batch(BATCH_SIZE).repeat()

    val_data_single = tf.data.Dataset.from_tensor_slices((x_val_single, y_val_single))
    val_data_single = val_data_single.batch(BATCH_SIZE).repeat()

    single_step_model = tf.keras.models.Sequential()
    single_step_model.add(tf.keras.layers.LSTM(32, input_shape=x_train_single.shape[-2:]))
    single_step_model.add(tf.keras.layers.Dense(1))
    single_step_model.compile(optimizer='adam', loss='mae') # tf.keras.optimizers.RMSprop()
    early_stopping_monitor = EarlyStopping(monitor='val_loss', min_delta=0, patience = 8, verbose=1, mode='auto', baseline=None, restore_best_weights=True)

    # optimizers adam?
    for x, y in val_data_single.take(1):
        print(single_step_model.predict(x).shape)

    single_step_history = single_step_model.fit(train_data_single, epochs=EPOCHS,
                                                steps_per_epoch=EVALUATION_INTERVAL,
                                                validation_data=val_data_single,
                                                validation_steps=140,
                                                callbacks=[early_stopping_monitor])


    def plot_train_history(history, title):
        loss = history.history['loss']
        val_loss = history.history['val_loss']

        epochs = range(len(loss))

        plt.figure()

        plt.plot(epochs, loss, 'b', label='Training loss')
        plt.plot(epochs, val_loss, 'r', label='Validation loss')
        plt.title(title)
        plt.legend()

        plt.show()

    #plot_train_history(single_step_history,
    #                   'Single Step Training and Validation Loss')


    #머신러닝 저장
    # 구역별로, 필요한 시간 별로 저장해야한다.
    modelfilename = '/content/drive/MyDrive/Colab Notebooks/dust/최종쓸파일들/모델파일들/'+ location+'/'+location+str(future_num)+'.h5'
    single_step_model.save(modelfilename)

