/**
 * 
 */
package PageOModel;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author JMukherjee
 *
 */
public class EditFormControls {
	
	WebElement driver;

	public EditFormControls(WebElement pdriver) {
		this.driver= pdriver;
		
//		this.rootElement=rootElement;

	}
	
	/**
	 * identify all the checkBoxes
	 */
	public void setCheckBox(){
		
	}
	
	/**
	 * identify all the text boxes
	 */
	List<WebElement> textBoxes;
	
	public void setText(String text,String textElement){
			for(WebElement textBox:textBoxes){
				if(textBox.getText().equalsIgnoreCase(textElement)){
					textBox.sendKeys(text);
				}
			}
	}
	
	/**
	 * identify submit button
	 */
	@FindBy
	WebElement submitButton; 
	public void submitForm(){
		submitButton.click();
	}
}
