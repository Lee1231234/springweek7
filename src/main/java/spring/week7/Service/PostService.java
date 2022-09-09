package spring.week7.Service;


import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spring.week7.Dto.Request.PostRequestDto;
import spring.week7.Dto.Response.PostResponseDto;
import spring.week7.Repository.PostRepository;
import spring.week7.Util.S3Uploader;
import spring.week7.domain.Member;
import spring.week7.domain.Post;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;
    // 게시물 상세내용 가져오기
    public Post findPostByID(Long id) {

        return postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 id 게시물이 없습니다")
        );
    }

    // 게시물 생성
    @Transactional
    public Post postCreate(PostRequestDto postRequestDto, MultipartFile image, Member member) throws IOException {

        String postImage = s3Uploader.upload(image, "static");
        Post post = new Post(postRequestDto,postImage, member);
        return postRepository.save(post);
    }

    // 게시물 내용 수정
    @Transactional
    public Post postEdit(Long id, PostRequestDto postRequestDto,MultipartFile image, Member member) throws IOException {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 id 게시물이 없습니다")
        );
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        String imageUrl = post.getImage();
        //이미지 존재시 먼저 삭제후 다시 업로드.
        if(imageUrl!= null) {
            String deleteUrl = imageUrl.substring(imageUrl.indexOf("static"));
            s3Uploader.deleteImage(deleteUrl);
            imageUrl = s3Uploader.upload(image,"static");

        }
        post.update(postRequestDto, imageUrl);

        return post;
    }

    // 게시물 삭제
    @Transactional
    public Long postDelete(Long id, Member member) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 id 게시물이 없습니다")
        );
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        postRepository.deleteById(id);
        return id;
    }


    // 게시물 검색
    public List<PostResponseDto> postSearch(String keyword) {
        //제목검색
        List<Post> postListTitle = postRepository.findByTitleContaining(keyword);
        //내용검색
        List<Post> postListContent = postRepository.findByContentContaining(keyword);

        //제목, 내용검색 합치기
        postListTitle.addAll(postListContent);
        //LinkedHashSet로 변환(중복제거)
        Set<Post> LinkedTitle = new LinkedHashSet<>(postListTitle);
        //리스트로 변환
        ArrayList<Post> ResultList = new ArrayList<>(LinkedTitle);

        List<PostResponseDto> result = ResultList.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());


        return result;
    }


}//class
