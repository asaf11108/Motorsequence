package database;

import android.content.Context;

/**
 * This class stores single tone classes of tables entries.
 */
public class FactoryEntry {
    private final Context mContext;
    private static FactoryEntry mFactoryEntry;


    private FactoryEntry(Context Context) {
        mContext = Context;
    }

    public static void createFactoryEntry(Context context){
        if(mFactoryEntry == null) mFactoryEntry = new FactoryEntry(context);

    }

    /**
     * create single tone Participant entry.
     *
     * @return ParticipantEntry
     */
    public static ParticipantEntry getParticipantEntry(){
        return ParticipantEntry.getInstance(mFactoryEntry.mContext);

    }

    /**
     * create single tone TestSetEntry entry.
     *
     * @return TestSetEntry
     */
    public static TestSetEntry getTestSetEntry(){
        return TestSetEntry.getInstance(mFactoryEntry.mContext);

    }

    /**
     * create single tone TestSetEntry entry.
     *
     * @return TestSetEntry
     */
    public static TestTypeEntry getTestTypeEntry(){
        return TestTypeEntry.getInstance(mFactoryEntry.mContext);

    }

    /**
     * create single tone TestSetEntry entry.
     *
     * @return TestSetEntry
     */
    public static RecordTestEntry getRecordTestEntry(){
        return RecordTestEntry.getInstance(mFactoryEntry.mContext);

    }

    /**
     * create single tone TestSetEntry entry.
     *
     * @return TestSetEntry
     */
    public static RecordRoundEntry getRecordRoundEntry(){
        return RecordRoundEntry.getInstance(mFactoryEntry.mContext);

    }

    /**
     * create single tone TestSetEntry entry.
     *
     * @return TestSetEntry
     */
    public static XYRoundEntry getXYRoundEntry(){
        return XYRoundEntry.getInstance(mFactoryEntry.mContext);

    }
}
