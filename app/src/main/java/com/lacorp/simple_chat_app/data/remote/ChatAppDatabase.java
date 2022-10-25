package com.lacorp.simple_chat_app.data.remote;

import static com.lacorp.simple_chat_app.utils.Constants.USER_COLLECTION;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lacorp.simple_chat_app.data.entities.User;

import java.util.Collections;
import java.util.List;

public class ChatAppDatabase {

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference usersCollection = firestore.collection(USER_COLLECTION);

    public List<User> getAllUsers() {
        List<User> userList;

        try {
            userList = usersCollection.get().getResult().toObjects(User.class);
        }
        catch (Exception ex) {
            userList = Collections.emptyList();
        }

        return userList;
    }
}
