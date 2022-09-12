package spring.week7.Service;


import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import spring.week7.Dto.Request.PostRequestDto;
import spring.week7.Dto.Response.PostResponseDto;
import spring.week7.Repository.PostCategoryRepository;
import spring.week7.Repository.PostRepository;
import spring.week7.domain.Member;
import spring.week7.domain.Post;
import spring.week7.domain.PostCategory;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostCategoryRepository postCategoryRepository;

    // 게시물 상세내용 가져오기
    public Post findPostByID(Long id) {

        return postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 id 게시물이 없습니다")
        );
    }

    // 게시물 생성
    @Transactional
    public Post postCreate(PostRequestDto postRequestDto, Member member)  {

        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .postCategory(postCategoryRepository.findByName(postRequestDto.getPostCategory().getName()))
                .member(member)
                .build();
        return postRepository.save(post);
    }

    // 게시물 내용 수정
    @Transactional
    public Post postEdit(Long id, PostRequestDto postRequestDto, Member member) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 id 게시물이 없습니다")
        );
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        post.update(postRequestDto);
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

    //카테고리 추가
    public PostCategory categoryAdd(String categoryName) {

        PostCategory postCategory = new PostCategory();
        postCategory.setName(categoryName);

        return postCategoryRepository.save(postCategory);
    }


    //게시글 전체조회
    //카테고리 별 목록 조회 추가
    public Page<Post> postAllList(Model model, Long postCategoryId, Pageable pageable) {
        Page<Post> postPage;

        if (postCategoryId > 0) {
            postPage = postRepository.findByPostCategoryId(postCategoryId, pageable);
            model.addAttribute("categoryId", postCategoryId);
            return postPage;

        } else {
            postPage = postRepository.findAll(pageable);
        }

//        model.addAttribute("categoryId", postCategoryId);
        model.addAttribute("postPage", postPage);

        return postPage;
    }


}//class
