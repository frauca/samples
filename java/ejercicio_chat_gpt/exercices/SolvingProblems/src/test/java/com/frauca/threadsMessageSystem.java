package com.frauca;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class threadsMessageSystem {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.SSS");
    static void log(String message) {
        System.out.println("%s[%s] -- %s"
                .formatted(
                        Thread.currentThread().getName(),
                        dateFormat.format(System.currentTimeMillis()),
                        message
                ));
    }
    record User(String name) {}
    static class UserSimulator {
        private final User me;
        private final BaseService messageService;
        private final List<User> allUsersButMe;
        public UserSimulator(User me,BaseService messageService, List<User> users) {
            this.me = me;
            this.messageService = messageService;
            this.allUsersButMe = users.stream().filter(u->!u.name().equals(me.name())).toList();
        }
        public void receiveMessage(User origin, String message) {
            replyMessage(origin,message);
            //log("User %s received message from %s: %s%n".formatted( me.name(), origin.name(), message));
            gossiphMessage(origin, message);
            //log("User %s finished processing message from %s".formatted( me.name(), origin.name()));
        }
        public void replyMessage(User origin, String message) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            think();
            String reply = message;
            if (!reply.contains("Reply")){
                reply = "Reply "+message;
            }
            //log("User %s replying to %s: %s%n".formatted( me.name(), origin.name(), reply));
            messageService.sendMessage(me, origin, reply);
        }
        public void gossiphMessage(User origin, String message) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for(int i=0;i<random.nextInt(10);i++){
                think();
                String reply = message;
                var goshingUser = allUsersButMe.get(random.nextInt(allUsersButMe.size()));
                //log("User %s gossiping to %s: %s%n".formatted( me.name(), goshingUser.name(), reply));
                messageService.sendMessage(me, goshingUser, reply);
            }
        }

        private void think(){
            ThreadLocalRandom random = ThreadLocalRandom.current();
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextLong(500, 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class BaseService {
        private AtomicLong sentMessages = new AtomicLong(0);
        private final Map<String,UserSimulator> users;
        private final ExecutorService messagesRunner;

        BaseService(List<User> users, ExecutorService messagesRunner) {
            this.users = users.stream()
                    .collect(Collectors.toMap(User::name, user -> new UserSimulator(user, this, users)));
            this.messagesRunner = messagesRunner;
        }

        public void sendMessage(User origin, User destination, String message) {
            messagesRunner.submit(()->{
                UserSimulator destSim = users.get(destination.name());
                destSim.receiveMessage(origin, message);
                var currentSent = sentMessages.incrementAndGet();
                if(currentSent % 1000 == 0){
                    log("Total messages sent: %s".formatted(currentSent));
                }
            });
        }

        public void stop() {
            this.messagesRunner.shutdown();
            try {
                this.messagesRunner.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public long numberOfSentMessages() {
            return sentMessages.get();
        }
    }

    List<User> createUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new User("User" + i))
                .toList();
    }

    void startNoise(BaseService messageService, List<User> users) {
        for(int i=0; i<users.size(); i++) {
            User origin = users.get(i);
            User destination = users.get((i+1) % users.size());
            String message = "Hello from " + origin.name() + " to " + destination.name();
            messageService.sendMessage(origin, destination, message);
        }
    }

    @Test
    void testMessageSystem() {
        List<User> users = createUsers(5_000);
        ExecutorService messagesRunner = Executors.newFixedThreadPool(100);
        BaseService messageService = new BaseService(users, messagesRunner);
        var normalElapsed = Elapsed.run(() -> {
            startNoise(messageService, users.stream().limit(100).toList());
            while (messageService.numberOfSentMessages() < 1_000) {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        messageService.stop();
        ExecutorService virutalExecutor = Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("vthread-",0).factory()
        );
        BaseService virtualMessageService = new BaseService(users, virutalExecutor);
        var virtualElapsed = Elapsed.run(() -> {
            startNoise(virtualMessageService, users);
            while (virtualMessageService.numberOfSentMessages() < 50_000) {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        virtualMessageService.stop();

        assertTrue(virtualElapsed.timeMillis() < normalElapsed.timeMillis());
    }

}
