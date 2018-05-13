package org.kusmp.api;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MorphiaHandler {
    private static MorphiaHandler Instance = new MorphiaHandler();
    private static Datastore datastore;

    public MorphiaHandler() {
        final Morphia morphia = new Morphia();
        morphia.mapPackage("org.kusmp.api");
        datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "mongo_database");
        datastore.ensureIndexes();
    }

    public static MorphiaHandler getInstance() {
        return Instance;
    }

    public static Datastore getDatastore() {
        return datastore;
    }
}
