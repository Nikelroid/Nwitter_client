package twitteControl;

import category.Category;
import category.categoryMembers;
import chat.chatPage;
import chat.newChat;
import comments.commentsPage;
import groups.gpMain;
import groups.gpMembers;
import lists.Lists;
import lists.likeretList;
import lists.userSelect;
import mainPages.*;
import messengerChilds.savedTwittes;
import notifications.Notifications;
import notifications.oRequests;
import twitteHyperlink.showHyperlink;
import userControl.userProfile;

public class deathPages {
    public deathPages() {
        Feed.vValueFeed = TwitteController.scrollPane.getVvalue();
        if(Feed.updateChecker!=null)
        Feed.updateChecker.stop();
        if(userTwittes.updateChecker!=null)
            userTwittes.updateChecker.stop();
        if(userProfile.updateChecker!=null)
            userProfile.updateChecker.stop();
        if(Info.updateChecker!=null)
            Info.updateChecker.stop();
        if(commentsPage.updateChecker!=null)
            commentsPage.updateChecker.stop();
        if(likeretList.updateChecker!=null)
            likeretList.updateChecker.stop();
        if(Expelorer.updateChecker!=null)
            Expelorer.updateChecker.stop();
        if(Lists.updateChecker!=null)
            Lists.updateChecker.stop();
        if(Setting.updateChecker!=null)
            Setting.updateChecker.stop();
        if(Category.updateChecker!=null)
            Category.updateChecker.stop();
        if(categoryMembers.updateChecker1!=null)
            categoryMembers.updateChecker1.stop();
        if(categoryMembers.updateChecker2!=null)
            categoryMembers.updateChecker2.stop();
        if(chatPage.updateChecker!=null)
            chatPage.updateChecker.stop();
        if(Messenger.updateChecker!=null)
            Messenger.updateChecker.stop();
        if(userSelect.updateChecker!=null)
            userSelect.updateChecker.stop();
        if(newChat.updateChecker!=null)
            newChat.updateChecker.stop();
        if(savedTwittes.updateChecker!=null)
            savedTwittes.updateChecker.stop();
        if(gpMain.updateChecker!=null)
            gpMain.updateChecker.stop();
        if(gpMembers.updateChecker1!=null)
            gpMembers.updateChecker1.stop();
        if(gpMembers.updateChecker2!=null)
            gpMembers.updateChecker2.stop();
        if(Notifications.updateChecker!=null)
            Notifications.updateChecker.stop();
        if(oRequests.updateChecker!=null)
            oRequests.updateChecker.stop();
        if(showHyperlink.updateChecker!=null)
            showHyperlink.updateChecker.stop();
        if(showHyperlink.updateChecker!=null)
            showHyperlink.updateChecker.stop();
    }
}
