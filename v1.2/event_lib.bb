Type evt_obj
	Field code
	Field o_x
	Field o_y
	Field o_z
	Field o_dx#
	Field o_dy#
	Field o_dz#
	Field o_type
	Field o_moveid
	Field custom1
	Field custom2
	Field custom3
	Field custom4
	Field custom5
	Field time
End Type

Const evt_max=10000
Dim evt_list.evt_obj(evt_max)
Global evt_ctr
Global evt_remember_to_delete.evt_obj

Function Evt_Init()	
	evt_ctr=0
	evt_remember_to_delete=Null 
End Function 

Function Evt_Free()	
	evt_ctr=0
	evt_remember_to_delete=Null 
	
	For te.evt_obj = Each evt_obj
		Delete te
	Next 
	
End Function 

Function Evt_Get.evt_obj ()

	Return Evt_Get_Int(MilliSecs())
	
End Function 


Function Evt_Put.evt_obj(code,time_in_future)

	Return Evt_Put_Int(code,time_in_future+MilliSecs())
	
End Function 


Function Evt_Get_Int.evt_obj (time_now)


	If(evt_remember_to_delete<>Null) Then
		Delete evt_remember_to_delete
		evt_remember_to_delete=Null 
	EndIf
	
	If(evt_ctr=0) Return Null 
	
	o1.evt_obj=evt_list(evt_ctr-1)
	If(time_now >= o1\time )  Then
		evt_ctr=evt_ctr-1
		evt_remember_to_delete=o1
		Return o1
	EndIf 
	
End Function 

Function Evt_Put_Int.evt_obj(code,time_in_future)
	
	newtime=time_in_future
	
	onew.evt_obj=New evt_obj
	
	dst1=evt_ctr
	
	onew\code=code
	onew\time=time_in_future
		
	destix=evt_ctr
		
	For t=evt_ctr-1 To 0 Step -1
		o1.evt_obj=evt_list(t)
		If o1\time>time_in_future Then Exit 
		destix=t
	Next 
	

	If(destix<>dst1) Then 
		For t=evt_ctr-1 To destix Step -1
		
			evt_list(t+1)=evt_list(t)
	
		Next 
	EndIf  
	evt_list(destix)=onew
	evt_ctr=evt_ctr+1
	
	Return onew
	;DebugLog "i("+time_in_future+")"+destix+","+dst1
	
End Function 
