package org.superfights.statisticmanagement.resources;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

@ApplicationScoped
@ServerEndpoint("/stats/team")
public class TeamStatsWebSocket {

	private static final Logger LOGGER = Logger.getLogger(TeamStatsWebSocket.class);

	private List<Session> sessions = new CopyOnWriteArrayList<>();
	private Disposable subscription;

	@Inject
	@Channel("team-stats")
	Flowable<Double> stream;

	@OnOpen
	public void onOpen(Session session) {
		sessions.add(session);
	}

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
	}

	@PostConstruct
	public void subscribe() {
		subscription = stream.subscribe(ratio -> sessions.forEach(session -> write(session, ratio)));
	}

	@PreDestroy
	public void cleanup() {
		subscription.dispose();
	}

	private void write(Session session, double ratio) {
		session.getAsyncRemote().sendText(Double.toString(ratio), result -> {
			if (result.getException() != null) {
				LOGGER.error("Unable to write message to web socket", result.getException());
			}
		});
	}

}
