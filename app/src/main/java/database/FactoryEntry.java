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
    public static TestSetEntry getTestSetEntryEntry(){
        return TestSetEntry.getInstance(mFactoryEntry.mContext);

    }
}
