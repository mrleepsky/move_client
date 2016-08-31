package mlipa.move.client;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHandler.class.toString();

    public static final String DATABASE_NAME = "m_move.db";
    public static final int DATABASE_VERSION = 2;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createActivitiesTable = "CREATE TABLE " + ActivitiesContract.Activities.TABLE_NAME + " (" +
                ActivitiesContract.Activities._ID + " INTEGER PRIMARY KEY UNIQUE NOT NULL, " +
                ActivitiesContract.Activities.COLUMN_NAME_NAME + " VARCHAR(32) UNIQUE NOT NULL);";

        String createClassifiersTable = "CREATE TABLE " + ClassifiersContract.Classifiers.TABLE_NAME + " (" +
                ClassifiersContract.Classifiers._ID + " INTEGER PRIMARY KEY UNIQUE NOT NULL, " +
                ClassifiersContract.Classifiers.COLUMN_NAME_NAME + " VARCHAR(32) UNIQUE NOT NULL);";

        String createUsersTable = "CREATE TABLE " + UsersContract.Users.TABLE_NAME + " (" +
                UsersContract.Users._ID + " INTEGER PRIMARY KEY UNIQUE NOT NULL, " +
                UsersContract.Users.COLUMN_NAME_USERNAME + " VARCHAR(32) UNIQUE NOT NULL);";

        String createRawsTable = "CREATE TABLE " + RawContract.Raws.TABLE_NAME + " (" +
                RawContract.Raws._ID + " INTEGER PRIMARY KEY UNIQUE NOT NULL, " +
                RawContract.Raws.COLUMN_NAME_TIMESTAMP + " DATETIME NOT NULL, " +
                RawContract.Raws.COLUMN_NAME_ACTIVITY_ID + " INTEGER NOT NULL, " +
                RawContract.Raws.COLUMN_NAME_USER_ID + " INTEGER NOT NULL, " +
                RawContract.Raws.COLUMN_NAME_X + " DOUBLE NOT NULL, " +
                RawContract.Raws.COLUMN_NAME_Y + " DOUBLE NOT NULL, " +
                RawContract.Raws.COLUMN_NAME_Z + " DOUBLE NOT NULL, " +
                "FOREIGN KEY (" + RawContract.Raws.COLUMN_NAME_ACTIVITY_ID + ") REFERENCES " + ActivitiesContract.Activities.TABLE_NAME + "(" + ActivitiesContract.Activities._ID + "), " +
                "FOREIGN KEY (" + RawContract.Raws.COLUMN_NAME_USER_ID + ") REFERENCES " + UsersContract.Users.TABLE_NAME + "(" + UsersContract.Users._ID + "));";

        new QualitiesContract.Qualities();

        String createQualitiesTable = "CREATE TABLE " + QualitiesContract.Qualities.TABLE_NAME + " (" +
                QualitiesContract.Qualities._ID + "  INTEGER PRIMARY KEY UNIQUE NOT NULL, " +
                QualitiesContract.Qualities.COLUMN_NAME_TIMESTAMP_START + " DATETIME NOT NULL, " +
                QualitiesContract.Qualities.COLUMN_NAME_TIMESTAMP_STOP + " DATETIME NOT NULL, " +
                QualitiesContract.Qualities.COLUMN_NAME_ACTIVITY_ID + " INTEGER NOT NULL, " +
                QualitiesContract.Qualities.COLUMN_NAME_USER_ID + " INTEGER NOT NULL, ";

        for (int i = 0; i <= QualitiesContract.Qualities.QUALITIES_NUMBER - 1; i++) {
            createQualitiesTable += QualitiesContract.Qualities.COLUMN_NAME_QUALITIES[i] + " DOUBLE NOT NULL, ";
        }

        createQualitiesTable += "FOREIGN KEY (" + QualitiesContract.Qualities.COLUMN_NAME_ACTIVITY_ID + ") REFERENCES " + ActivitiesContract.Activities.TABLE_NAME + "(" + ActivitiesContract.Activities._ID + "), " +
                "FOREIGN KEY (" + QualitiesContract.Qualities.COLUMN_NAME_USER_ID + ") REFERENCES " + UsersContract.Users.TABLE_NAME + "(" + UsersContract.Users._ID + "));";

        new WeightsContract.Weights();

        String createWeightsTable = "CREATE TABLE " + WeightsContract.Weights.TABLE_NAME + " (" +
                WeightsContract.Weights._ID + "  INTEGER PRIMARY KEY UNIQUE NOT NULL, " +
                WeightsContract.Weights.COLUMN_NAME_CLASSIFIER_ID + " INTEGER NOT NULL, ";

        for (int i = 0; i <= WeightsContract.Weights.WEIGHTS_NUMBER - 1; i++) {
            createWeightsTable += WeightsContract.Weights.COLUMN_NAME_WEIGHT[i] + " DOUBLE NOT NULL, ";
        }

        createWeightsTable += "FOREIGN KEY (" + WeightsContract.Weights.COLUMN_NAME_CLASSIFIER_ID + ") REFERENCES " + ClassifiersContract.Classifiers.TABLE_NAME + "(" + ClassifiersContract.Classifiers._ID + "));";

        String createPredictionsTable = "CREATE TABLE " + PredictionsContract.Predictions.TABLE_NAME + " (" +
                PredictionsContract.Predictions._ID + "  INTEGER PRIMARY KEY UNIQUE NOT NULL, " +
                PredictionsContract.Predictions.COLUMN_NAME_TIMESTAMP + " DATETIME NOT NULL, " +
                PredictionsContract.Predictions.COLUMN_NAME_ACTIVITY_ID + " INTEGER NOT NULL, " +
                PredictionsContract.Predictions.COLUMN_NAME_CLASSIFIER_ID + " INTEGER NOT NULL, " +
                PredictionsContract.Predictions.COLUMN_NAME_USER_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + PredictionsContract.Predictions.COLUMN_NAME_ACTIVITY_ID + ") REFERENCES " + ActivitiesContract.Activities.TABLE_NAME + "(" + ActivitiesContract.Activities._ID + "), " +
                "FOREIGN KEY (" + PredictionsContract.Predictions.COLUMN_NAME_CLASSIFIER_ID + ") REFERENCES " + ClassifiersContract.Classifiers.TABLE_NAME + "(" + ClassifiersContract.Classifiers._ID + "), " +
                "FOREIGN KEY (" + PredictionsContract.Predictions.COLUMN_NAME_USER_ID + ") REFERENCES " + UsersContract.Users.TABLE_NAME + "(" + UsersContract.Users._ID + "));";

        database.execSQL(createActivitiesTable);
        database.execSQL(createClassifiersTable);
        database.execSQL(createUsersTable);
        database.execSQL(createRawsTable);
        database.execSQL(createQualitiesTable);
        database.execSQL(createWeightsTable);
        database.execSQL(createPredictionsTable);

        String insertActivities = "INSERT INTO " + ActivitiesContract.Activities.TABLE_NAME + " (" +
                ActivitiesContract.Activities.COLUMN_NAME_NAME + ") VALUES " +
                "('Lie'), " +
                "('Sit'), " +
                "('Stand'), " +
                "('Walk');";

        String insertClassifiers = "INSERT INTO " + ClassifiersContract.Classifiers.TABLE_NAME + "(" +
                ClassifiersContract.Classifiers.COLUMN_NAME_NAME + ") VALUES " +
                "('Artificial neural network');";

        database.execSQL(insertActivities);
        database.execSQL(insertClassifiers);

        Log.v(TAG, DATABASE_NAME + " (version " + DATABASE_VERSION + ") created successfully!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            String dropActivitiesTable = "DROP TABLE IF EXISTS " + ActivitiesContract.Activities.TABLE_NAME + ";";
            String dropClassifiersTable = "DROP TABLE IF EXISTS " + ClassifiersContract.Classifiers.TABLE_NAME + ";";
            String dropUsersTable = "DROP TABLE IF EXISTS " + UsersContract.Users.TABLE_NAME + ";";
            String dropRawsTable = "DROP TABLE IF EXISTS " + RawContract.Raws.TABLE_NAME + ";";
            String dropQualitiesTable = "DROP TABLE IF EXISTS " + QualitiesContract.Qualities.TABLE_NAME + ";";
            String dropWeightsTable = "DROP TABLE IF EXISTS " + WeightsContract.Weights.TABLE_NAME + ";";
            String dropPredictionsTable = "DROP TABLE IF EXISTS " + PredictionsContract.Predictions.TABLE_NAME + ";";

            database.execSQL(dropPredictionsTable);
            database.execSQL(dropWeightsTable);
            database.execSQL(dropQualitiesTable);
            database.execSQL(dropRawsTable);
            database.execSQL(dropUsersTable);
            database.execSQL(dropClassifiersTable);
            database.execSQL(dropActivitiesTable);

            Log.v(TAG, DATABASE_NAME + " (version " + DATABASE_VERSION + ") dropped successfully!");

            onCreate(database);
        }
    }
}
