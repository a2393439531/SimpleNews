package de.dala.simplenews.database;


import static de.dala.simplenews.database.DatabaseHandler.ENTRY_VISITED_DATE;
import static de.dala.simplenews.database.DatabaseHandler.concatenateQueries;

/**
 * Created by Daniel on 01.08.2014.
 */
class PersistableUnreadEntries extends PersistableEntries{

    PersistableUnreadEntries(Long categoryId, Long feedId, Long entryId, Boolean onlyVisible){
        super(categoryId, feedId, entryId, onlyVisible);
    }

    @Override
    protected String getQuery() {
        return concatenateQueries(super.getQuery(), ENTRY_VISITED_DATE + " IS NULL");
    }
}
