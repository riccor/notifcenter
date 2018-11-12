*** Settings ***
Library  	RequestsLibrary
Library		Collections


*** Variables ***

${HOST}		http://localhost:8080/notifcenter/apiaplicacoes


*** Keywords ***

Connect to RESTful webservice
	Create Session		mysession		${HOST}

Request application ${APPLICATION_ID} viewaplicacao data 
	${RESPOSTA}=			Get Request		mysession		/oauth/viewaplicacao/${APPLICATION_ID}
	Log						Resposta: ${RESPOSTA.json()}
	Set Test Variable		${RESPOSTA}

Check if status code is ${EXPECTED_STATUS_CODE}
	Should Be Equal As Strings		${RESPOSTA.status_code}		${EXPECTED_STATUS_CODE}	
	Log								Status code esperado: ${EXPECTED_STATUS_CODE} --- status code obtido: ${RESPOSTA.status_code}

Check if application name is ${EXPECTED_APPLICATION_NAME}
	Dictionary Should Contain Item		${RESPOSTA.json()}				name		${EXPECTED_APPLICATION_NAME}	



Request application ${APPLICATION_ID} to add remetente ${REMETENTE_NAME} data 
	${RESPOSTA}=			Post Request		mysession		/${APPLICATION_ID}/addremetente?name=${REMETENTE_NAME}
	Log						Resposta: ${RESPOSTA.json()}
	Set Test Variable		${RESPOSTA}

Check if remetente name is ${EXPECTED_REMETENTE_NAME}
	Dictionary Should Contain Item		${RESPOSTA.json()}				name		${EXPECTED_REMETENTE_NAME}	

Save remetente ID
	${SAVED_REMETENTE_ID}=		Get From Dictionary			${RESPOSTA.json()}			id
	Log							O ID do remetente é: ${SAVED_REMETENTE_ID}
	Set Suite Variable			${SAVED_REMETENTE_ID}



Request application ${APPLICATION_ID} to add notification channel for saved remetente id and channel ${CHANNEL_ID}
	${RESPOSTA}=		Post Request		mysession		/${APPLICATION_ID}/pedidocanalnotificacao?canal=${CHANNEL_ID}&remetente=${SAVED_REMETENTE_ID}
	Log						Resposta: ${RESPOSTA.json()}
	Set Test Variable		${RESPOSTA}

Save notification channel ID
	${SAVED_NOTIFICATION_CHANNEL_ID}=		Get From Dictionary			${RESPOSTA.json()}			id
	Log										O ID do notification channel é: ${SAVED_NOTIFICATION_CHANNEL_ID}
	Set Suite Variable						${SAVED_NOTIFICATION_CHANNEL_ID}



Send message to whatsapp from application ${APPLICATION_ID} to group ${GROUP_ID}
	${RESPOSTA}=		Post Request		mysession		/${APPLICATION_ID}/sendmessage?canalnotificacao=${SAVED_NOTIFICATION_CHANNEL_ID}&gdest=${GROUP_ID}&assunto=umassunto2&textocurto=mensagem do robot 3&textolongo=algumtextolongo2
	Log					Resposta: ${RESPOSTA.json()}








