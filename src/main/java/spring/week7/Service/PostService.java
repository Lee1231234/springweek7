package spring.week7.Service;


import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import spring.week7.Dto.Request.PostRequestDto;
import spring.week7.Dto.Response.PostResponseDto;
import spring.week7.Errorhandler.ApiRequestException;
import spring.week7.Repository.BoardRepository;
import spring.week7.Repository.PostRepository;
import spring.week7.Util.S3Uploader;
import spring.week7.domain.Board;
import spring.week7.domain.Member;
import spring.week7.domain.Post;

import javax.transaction.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;
    private final BoardRepository boardRepository;

    // 게시물 상세내용 가져오기
    public PostResponseDto findPostByID(Long id) {
       Post post = postRepository.findByJoinComment(id).orElseThrow(
                () -> new NullPointerException("해당 id 게시물이 없습니다")
        );

        return new PostResponseDto(post);
    }

    // 게시물 생성
    @Transactional
    public Post postCreate(PostRequestDto postRequestDto, MultipartFile image, Member member) throws IOException {

        String postImage = s3Uploader.upload(image, "static");
        Post post = new Post(postRequestDto, postImage, member);
        return postRepository.save(post);
    }

    // 게시물 내용 수정
    @Transactional
    public Post postEdit(Long id, PostRequestDto postRequestDto, MultipartFile image, Member member) throws IOException {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 id 게시물이 없습니다")
        );
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        String imageUrl = post.getImage();
        //이미지 존재시 먼저 삭제후 다시 업로드.
        if (imageUrl != null) {
            String deleteUrl = imageUrl.substring(imageUrl.indexOf("static"));
            s3Uploader.deleteImage(deleteUrl);
            imageUrl = s3Uploader.upload(image, "static");

        }
        post.update(postRequestDto, imageUrl);

        return post;
    }

    // 게시물 삭제
    @Transactional
    public void postDelete(Long id, Member member) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 id 게시물이 없습니다")
        );
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        String image = post.getImage();
        String deleteUrl = image.substring(image.indexOf("static")); //이미지
        //s3에서 이미지 삭제
        s3Uploader.deleteImage(deleteUrl);
        postRepository.deleteById(id);

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

    //카테고리별 목록조회
    public Page<Post> postAllList(Model model, String category, Pageable pageable) {
        Page<Post> postPage;
        if (!(category == null)){
        Post.Category type = Post.Category.valueOf(category);
        //이미 저장된 게시물중 해당 카테고리인 게시물을 모두 불러오기
        switch (type) {
            case ANIMAL:
            case PLANT:
            case CITY:
            case SPACE:
            case TRAVEL:
            case FOOD:
                postPage = postRepository.findByCategory(category, pageable);
                model.addAttribute("category", category);
                return postPage;
            default:
                System.out.println("해당 카테고리가 없습니다.");
                break;
        }
        }
        postPage = postRepository.findAll(pageable);
        model.addAttribute("postPage", postPage);

        return postPage;
    }



    public Post postlike(PostRequestDto postRequestDto, Member member) {
        return null;
    }


    //게시물을 보드에 추가
    @Transactional
    public Post postAdd(Long id, Long boardId, Member member) {
        Post post = postRepository.findById(id).orElseThrow(
                ()-> new ApiRequestException("해당 게시물이 존재하지 않습니다.")
        );

        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new ApiRequestException("해당 보드가 존재하지 않습니다.")
        );

        Long memberId = member.getId();
        Long boardMemberId = board.getMember().getId();

        if(!memberId.equals(boardMemberId)){
            throw new ApiRequestException("해당 회원의 보드가 아닙니다.");
        }

        post.addBoard(board);
        return post;
    }
}//class
