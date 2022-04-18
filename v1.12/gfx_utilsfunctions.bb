Function GU_SetImageHandleCenter(myimage)

	iw=imagewidth(myimage)
	ih=imageheight(myimage)

	handleimage(myimage,iw/2,ih/2)

end Function 