package in.tech_camp.protospace_b.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.TagEntity;
import in.tech_camp.protospace_b.repository.TagRepository;
import lombok.AllArgsConstructor;

@Service
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

    public List<String> tagNameGetHook(List<String> tags) {
        return tags.stream().map(tag -> tagGetHook(tag)).collect(Collectors.toList());
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

    public List<String> tagNamePostHook(List<String> tags) {
        return tags.stream().map(tag -> tagPostHook(tag)).collect(Collectors.toList());
    }

    public List<TagEntity> tagPostHook(List<TagEntity> tags) {
        return tags.stream().map(tag -> tagPostHook(tag)).collect(Collectors.toList());
    }

    @Transactional
    public void updatePrototypeTags(PrototypeEntity prototype, List<String> tag_names) {
        List<String> postTagNames = tagNamePostHook(tag_names);
        try {
            tagRepository.purgeTagsFromPrototype(prototype);
        } catch (Exception e) {
            throw new Error("Cannot reset tags:" + e);
        }
        postTagNames.forEach(tagName -> {
            TagEntity tag;
            try {
                tag = tagRepository.justSameTag(tagName);
            } catch (Exception tagCannotFoundException) {
                System.out.println("Tag cannot found exception");
                tag = null;
            }
            if (tag == null) {
                try {
                    tag = new TagEntity();
                    tag.setTagName(tagName);
                    tagRepository.insert(tag);
                } catch (Exception e) {
                    System.out.println("e.toString() = " + e.toString());
                    Throwable cause = e.getCause();
                    while (cause != null) {
                        System.out.println("cause: " + cause.toString());
                        System.out.println("cause message: " + cause.getMessage());
                        cause = cause.getCause();
                    }
                    e.printStackTrace();
                    throw new Error("Cannot generate tag:" + tagName);
                }
            }
            try {
                tagRepository.setTagToPrototype(prototype, tag);
            } catch (Exception e) {
                throw new Error("Cannot set tag " + tag.getTagName() + ":" + tag.getId() + " to prototype:"
                        + prototype.getId());
            }
        });
    }
}