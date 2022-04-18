Include "event_lib.bb"

Const c_FPS=70
Global g_lastmilliseconds
Global g_FPSTIME#

g_FPSTIME#=1000/c_FPS


DebugLog "ms"+MilliSecs()


millisstart=MilliSecs() 

For t=1 To 10
 
	Evt_Put(19000+t,Rand(5000))
	
Next 

Evt_Put(-1,5500)


While Not (KeyHit(1))

	o.evt_obj=Evt_Get()
	If(o<>Null) Then
		If o\code<>-1 Then
			DebugLog "found ("+o\code+") event "+(MilliSecs()-millisstart)
		Else
			DebugLog "found END event"+(MilliSecs()-millisstart)
			Stop 
		EndIf 
	EndIf 
		
Wend 


Function LimitFPS()
	While MilliSecs()-g_lastmilliseconds < g_FPSTIME : Wend
	g_lastmilliseconds=MilliSecs()
End Function