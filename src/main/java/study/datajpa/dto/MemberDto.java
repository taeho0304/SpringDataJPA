package study.datajpa.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class MemberDto {
    private long id;
    private String userName;
    private String teamName;

    public MemberDto(long id, String userName, String teamName) {
        this.id = id;
        this.userName = userName;
        this.teamName = teamName;
    }
}
