package beats.babel.babelbeats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongDBService {

    private final SongRecordRepository songRecordRepository;
    private final BlackListRepository blackListRepository;

    @Autowired
    public SongDBService(SongRecordRepository EntityRepository, BlackListRepository BannedRepository) {
        this.songRecordRepository = EntityRepository;
        this.blackListRepository = BannedRepository;
    }

    public SongRecord addRecord(SongRecord newEntity) {
        return songRecordRepository.save(newEntity);
    }

    public BlackRecord banRecord(BlackRecord record) {
        return blackListRepository.save(record);
    }

    public boolean isSongInDatabase(String title) {
        SongRecord song = songRecordRepository.findByTitle(title);
        return song != null;
    }

    public boolean isSongBlacklisted(String title) {
        BlackRecord record = blackListRepository.findByTitle(title);
        return record != null;
    }

    public SongRecord getRecordByTitle(String title) {
        return songRecordRepository.findByTitle(title);
    }

    public long getNumberOfRecords() {
        return songRecordRepository.count();
    }

    public long getNumberOfBanned() {
        return blackListRepository.count();
    }
}