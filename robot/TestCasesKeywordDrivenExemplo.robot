*** Settings ***
Resource		Resource.robot
Test Setup			Log		test setup now
Test Teardown		Log		test tearndown now
Suite Setup			Log		suite setup now
Suite Teardown		Log		suite teardown now


*** Test Cases ***

Connect to Notifcenter API and view application name with a specific given id
	Connect to RESTful webservice
	Request application 281736969715714 viewaplicacao data
	Check if status code is 200
	Check if application name is app_77

Add remetente and save their id
	Request application 281736969715714 to add remetente ricardo data 
	Check if status code is 200
	Check if remetente name is ricardo
	Save remetente ID

Add notification channel for saved remetente ID
	Request application 281736969715714 to add notification channel for saved remetente id and channel 281835753963522
	Check if status code is 200
	Save notification channel ID

Send message to my whatsapp
	Send message to whatsapp from application 281736969715714 to group 281702609977345
	Check if status code is 200





