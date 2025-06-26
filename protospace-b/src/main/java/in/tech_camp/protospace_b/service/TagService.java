package in.tech_camp.protospace_b.service;

import java.util.List;
import java.util.stream.Collectors;

import in.tech_camp.protospace_b.entity.TagEntity;
import in.tech_camp.protospace_b.repository.TagRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TagService {
  private final TagRepository tagRepository;

  public String tagGetHook(String tag_name) {
    return tag_name;
  }
  public TagEntity tagGetHook(TagEntity tag) {
    TagEntity newTag = new TagEntity();
    newTag.setId(tag.getId());
    newTag.setTagName(tagGetHook(tag.getTagName()));
    return newTag;
  }
  public List<TagEntity> tagGetHook(List<TagEntity> tags) {
    return tags.stream().map(tag -> tagGetHook(tag)).collect(Collectors.toList());
  }

  public String tagPostHook(String tag_name) {
    return tag_name;
  }
  public TagEntity tagPostHook(TagEntity tag) {
    TagEntity newTag = new TagEntity();
    newTag.setId(tag.getId());
    newTag.setTagName(tagPostHook(tag.getTagName()));
    return newTag;
  }
  public List<TagEntity> tagPostHook(List<TagEntity> tags) {
    return tags.stream().map(tag -> tagPostHook(tag)).collect(Collectors.toList());
  }

  public List<TagEntity> pushAllTags(List<String> tag_names) {
    return tag_names.stream().map(tag_name -> {
      try {
        return tagRepository.justSameTag(tag_name);
      } catch (Exception e) {
      }
      try {
        return tagRepository.insert(tag_name);
      } catch (Exception e) {
        throw new Error("Cannot generate tag:" + tag_name);
      }
    }).collect(Collectors.toList());
  }
}