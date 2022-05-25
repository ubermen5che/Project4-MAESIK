package MAESIK.demo.service;

import MAESIK.demo.domain.*;
import MAESIK.demo.domain.dto.CommitDTO;
import MAESIK.demo.repository.CommitRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class CommitService {

    @Autowired
    CommitRepository commitRepository;

    public int saveCommitData(Member member, String repo, Long groupId) {
        DateTimeZone zone = DateTimeZone.forID("Asia/Seoul"); // Or DateTimeZone.UTC
        String url = "https://api.github.com/repos";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(url);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient wc = WebClient.builder().uriBuilderFactory(factory).baseUrl(url).build();

        Flux<CommitDTO> commitDTO = wc.get()
                .uri("/{name}/{repo}/commits", member.getGithubId(), repo)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CommitDTO.class);

        List<MemberGroup> memberGroupList = member.getMemberGroupList();

        for (MemberGroup memberGroup : memberGroupList) {
            Group group = memberGroup.getGroup();
            if (group.getId() == groupId) {
                List<MemberGroupRepoUrl> memberGroupRepoUrlList = memberGroup.getMemberGroupRepoUrlList();

                for (MemberGroupRepoUrl mgr : memberGroupRepoUrlList) {
                    if (mgr.getRepo().equals(repo)) {
                        AtomicReference<DateTime> dateTime = new AtomicReference<>();
                        List<CommitDAO> commitDAOS = mgr.getCommitEachRepo();
                        commitDTO.toStream().forEach(x -> {
                            DateTime dt = new DateTime(x.getCommit().getAuthor().getDate(), zone);
                            dateTime.set(dt);
                            long millisecondsSinceUnixEpoch = dateTime.get().getMillis();
                            Optional<CommitDAO> maxTimeCommitDAO = commitRepository.findDistinctTopByMemberGroupRepoIdOrderByCommitDateDesc(mgr.getMemberGroupRepoId());
                            if (maxTimeCommitDAO.isEmpty() || maxTimeCommitDAO.get().getCommitDate() < millisecondsSinceUnixEpoch) {
                                CommitDAO commitDAO = CommitDAO.builder()
                                        .sha(x.getSha())
                                        .name(x.getCommit().getAuthor().getName())
                                        .url(x.getUrl())
                                        .commitUrl(x.getCommit().getUrl())
                                        .commitDate(millisecondsSinceUnixEpoch)
                                        .email(x.getCommit().getAuthor().getEmail())
                                        .message(x.getCommit().getMessage())
                                        .memberGroupRepoId(mgr.getMemberGroupRepoId())
                                        .build();
                                commitDAOS.add(commitDAO);
                            }
                        });

                        // github open api에서 가져온 commit data중 가장 오래된 commit 시간
                        if (!commitDAOS.isEmpty()) {
                            commitRepository.saveAll(commitDAOS);
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                }
            }
        }
        return 0;
    }
}
