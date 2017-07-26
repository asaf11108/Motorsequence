package braude.motorsequence;

import android.provider.BaseColumns;

/**
 * Created by ASAF on 26/7/2017.
 * base on: https://developer.android.com/training/basics/data-storage/databases.html#DbHelper
 * base on: https://www.youtube.com/watch?v=T0ClYrJukPA
 */
public class DataContact {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DataContact() {}

    /* Inner class that defines the table contents */
    public static class ParticipantEntry implements BaseColumns {
        public static final String TABLE_NAME = "participant";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
