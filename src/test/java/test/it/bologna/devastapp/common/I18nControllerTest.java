package test.it.bologna.devastapp.common;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import test.it.bologna.devastapp.AbstractPresentationWebTest;

public class I18nControllerTest extends AbstractPresentationWebTest {
	@Autowired
	private MessageSource messages;

	@Test
	public void i18nTest() {
		// messages = new ReloadableResourceBundleMessageSource();
		String message = this.messages.getMessage("test.message",
				new Object[] { "UserName" }, "Required", null);
		System.out.println(message);
	}
}
