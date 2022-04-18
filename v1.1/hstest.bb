Include "highscore_lib.bb"

Global	hiscore_book$="test"
Global	hiscore_pw$="75750825672396539567392564987562389703784578095"
	
;Global	hiscore_host$="localhost"
;Global	hiscore_script$="/Lodgia.com/hiscores/score1.1.php"

Global	hiscore_host$="www.lodgia.com"
Global	hiscore_script$="/hiscores/score1.1.php"



;src$="name=%20Dusty&score=13621&difficulty=medium&level=1&homepage=www.lodgia.com"
;Print HSGetTagValue(src$,"name")
;Print HSGetTagValue(src$,"level")
;Print HSGetTagValue(src$,"difficulty")
;Print HSGetTagValue(src$,"homepage")
;Print HSGetTagValue(src$,"score")

;
;WaitKey
;End 

;Print ">"+URLDecode("hallo%22")

Highscores_Load()

SeedRnd MilliSecs() ; 
score=12121+Rand(1,1999)
name$="Dusty"
difficulty$="medium"
level=1
homepage$="www.lodgia.com"

DebugLog "score = "+score
SaveHiScore(hiscore_host$,hiscore_script$,80,hiscore_book$,hiscore_pw$,score,name$, difficulty$,level,homepage$)
;Function SaveScoreOnNet(webaddress$, script$, webport , scorebook$, password$,  score,name$, difficulty$,level$,homepage$)

;enc$=EncodeHSE(hiscore_pw$,"name=Dusty&difficulty=medium&level=1&homepage=www.lodgia.com")
;DebugLog "encoded:"+enc$

		For t = 0 To g_maxscores
		
			DebugLog "("+t+") "+g_names(t)+","+g_scores(t)+","+g_difficulty(t)+","+g_level(t)+","+g_homepage(t)

		Next
		
		
Function Highscores_Load()

	Highscore_Init("Super Typist")
	UpdateScoreFromNet(hiscore_host$,hiscore_script$,80 , hiscore_book$)
	
End Function 