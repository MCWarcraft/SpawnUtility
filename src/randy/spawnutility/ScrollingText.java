package randy.spawnutility;

public class ScrollingText {
	
	public String text;
	public String currentText = " ";
	public String previousText = " ";
	public String color;
	public int limitLow = 0;
	public int limitHigh = 16;
	public int count = 0;
	public int getCount = 0;
	public int previousGetCount = 0;
	
	public ScrollingText(String text, String color){
		this.text = text;
		this.color = color;
		if(text.length() + color.length() < 16)
			limitHigh = text.length() - color.length() - 1;
		
		//GetString(); //Set the text for the first "frame"
	}
	
	public void Update(){
		getCount++;
		int textLength = text.length();		
		if(limitHigh < textLength + color.length() && count == 0){
			limitLow++;
			limitHigh++;
		} else {
			count++;
			if(count == 20){
				limitLow = 0;
				if(text.length() + color.length() < 16)
					limitHigh = text.length() - color.length() - 1;
				else
					limitHigh = 16;
			} else if(count == 40){
				count = 0;
				limitLow++;
				limitHigh++;
			}
		}
	}
	
	public String GetString(){
		if((count == 0 || count == 20) && previousGetCount != getCount){
			previousText = currentText;
			currentText = color + text.substring(limitLow, limitHigh - color.length());
		}
		previousGetCount = getCount;
		
		return currentText;
	}
}
