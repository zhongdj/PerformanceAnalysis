package net.imadz.performance.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by geek on 8/17/14.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Reader {
    private String name;
    private String readerClass;
    private String defaultFile;
    private String description;
}
