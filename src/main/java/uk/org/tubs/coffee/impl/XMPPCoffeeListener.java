package uk.org.tubs.coffee.impl;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;

import com.eaio.util.text.HumanTime;

import uk.org.tubs.coffee.CoffeeListener;

public class XMPPCoffeeListener implements CoffeeListener {
	private String username, password, xmppServer = null;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setXmppServer(String xmppServer) {
		this.xmppServer = xmppServer;
	}

	private XMPPConnection conn;
	private CoffeeStatus previousStatus;
	private long lastFinishedBrewing;

	private void checkConnection() throws XMPPException {
		if (conn == null || !conn.isConnected()) {
			conn = new XMPPConnection(xmppServer);
			conn.connect();
			// conn.login("coffee", "c0ff33");
			conn.login(username, password);
			conn.getRoster().setSubscriptionMode(SubscriptionMode.accept_all);
		}
	}

	public void coffeStatusChanged(CoffeeStatus s) {
		try {
			checkConnection();
			conn.sendPacket(getPresence(s));
		} catch (XMPPException e) {
			throw new RuntimeException(e);
		}
	}

	private Presence getPresence(CoffeeStatus s) {
		// error presence shouldn't ever be returned
		Presence p = new Presence(Type.error);
		if (s.isBrewing()) {
			p = new Presence(Presence.Type.available, "Brewing�", 1, Mode.dnd);
		} else {
			// Not brewing not, were brewing last time...
			// We've just finished brewing! what time is it?
			if (previousStatus.isBrewing()) {
				lastFinishedBrewing = System.currentTimeMillis();
			}
		}

		if (!s.isBrewing() && s.isHotplateOn()) {
			// How long has the hotplate been on?
			String brewedAgo = HumanTime.approximately(System
			        .currentTimeMillis()
			        - lastFinishedBrewing);
			if ("".equals(brewedAgo)) {
				brewedAgo = "a couple of secs";
			}
			brewedAgo = "Brewed " + brewedAgo + " ago.";
			p = new Presence(Presence.Type.available, brewedAgo, 1,
			        Mode.available);
		}

		previousStatus = s;
		return p;
	}

}
