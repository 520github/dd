/**
 * 
 */
package me.twocoffee.service.event.message;

import me.twocoffee.entity.Message;
import me.twocoffee.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component
public class ShowThirdpartyFriendActionListener implements
		ApplicationListener<ShowThirdpartyFriendActionEvent> {

	@Autowired
	private MessageService messageService;

	@Override
	public void onApplicationEvent(ShowThirdpartyFriendActionEvent event) {
//		Message message = (Message) event.getSource();
		Message message = event.getMessage();
		messageService.delete(message.getId());
	}

}
