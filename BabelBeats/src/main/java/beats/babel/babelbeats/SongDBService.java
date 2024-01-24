package beats.babel.babelbeats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongDBService {

    private final SongRecordRepository songRecordRepository;

    @Autowired
    public SongDBService(SongRecordRepository yourEntityRepository) {
        this.songRecordRepository = yourEntityRepository;
    }

    public SongRecord addRecord(SongRecord newEntity) {
        return songRecordRepository.save(newEntity);
    }

    public boolean isSongInDatabase(String title) {
        SongRecord song = songRecordRepository.findByTitle(title);
        return song != null;
    }

    public SongRecord getRecordByTitle(String title) {
        return songRecordRepository.findByTitle(title);
    }

    public long getNumberOfRecords() {
        return songRecordRepository.count();
    }
}