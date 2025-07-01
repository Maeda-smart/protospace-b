package in.tech_camp.protospace_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.entity.ReadStatusEntity;
import in.tech_camp.protospace_b.repository.ReadStatusRepository;

@Service
public class ReadStatusService {

    @Autowired
    private ReadStatusRepository readStatusRepository;

    public void markAsRead(Integer prototypeId, Integer userId) {
        System.out.println("markAsRead called with prototypeId=" + prototypeId + ", userId=" + userId);
        ReadStatusEntity readStatusEntity = new ReadStatusEntity();
        readStatusEntity.setPrototypeId(prototypeId);
        readStatusEntity.setUserId(userId);
        readStatusRepository.insertOrUpdate(readStatusEntity);
        System.out.println("insertOrUpdate called");
    }

    public boolean isRead(Integer prototypeId, Integer userId) {
        return readStatusRepository.isRead(prototypeId, userId);
    }

    public List<ReadStatusEntity> findAllByUserId(Integer userId) {
        return readStatusRepository.findAllByUserId(userId);
    }
}
