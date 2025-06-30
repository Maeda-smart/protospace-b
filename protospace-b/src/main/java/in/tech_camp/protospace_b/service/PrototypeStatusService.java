package in.tech_camp.protospace_b.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.ReadStatusEntity;
import in.tech_camp.protospace_b.repository.NiceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// プロトタイプのステータスを管理(いいね数・いいね済みか・既読/未読)
public class PrototypeStatusService {

  private final NiceRepository niceRepository;
  private final ReadStatusService readStatusService;

  public Map<String, Map<Integer, ?>> generatePrototypeStatus(List<PrototypeEntity> prototypes, Integer userId) {
    Map<Integer, Integer> niceCountMap = new HashMap<>();
    Map<Integer, Boolean> isNiceMap = new HashMap<>();
    Map<Integer, Boolean> readStatusMap = new HashMap<>();

    // いいね数
    for (PrototypeEntity prototype : prototypes) {
      int count = niceRepository.countNiceByPrototypeId(prototype.getId());
      niceCountMap.put(prototype.getId(), count);
    }

    if (userId != null) {
      // いいね済みか
      for (PrototypeEntity prototype : prototypes) {
        boolean isNice = niceRepository.existNice(prototype.getId(), userId);
        isNiceMap.put(prototype.getId(), isNice);
      }

      // 既読/未読
      List<ReadStatusEntity> readList = readStatusService.findAllByUserId(userId);
      readStatusMap = readList.stream()
          .collect(Collectors.toMap(ReadStatusEntity::getPrototypeId, r -> true));

      // 自分が投稿したプロトタイプは既読扱いにする
      for (PrototypeEntity prototype : prototypes) {
        if (prototype.getUser() != null && prototype.getUser().getId().equals(userId)) {
          readStatusMap.put(prototype.getId(), true);
        }
      }

    // ログイン時以外はfalse
    } else {
      for (PrototypeEntity prototype : prototypes) {
        isNiceMap.put(prototype.getId(), false);
        readStatusMap.put(prototype.getId(), false);
      }
    }

    // それぞれの結果をresultにプット
    Map<String, Map<Integer, ?>> result = new HashMap<>();
    result.put("niceCountMap", niceCountMap);
    result.put("isNiceMap", isNiceMap);
    result.put("readStatusMap", readStatusMap);

    return result;
  }
}
