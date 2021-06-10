package com.example.Instagram.web;


import com.example.Instagram.dto.CommentDTO;
import com.example.Instagram.entity.Comment;
import com.example.Instagram.facade.CommentFacade;
import com.example.Instagram.payload.response.MessageResponse;
import com.example.Instagram.services.CommentService;
import com.example.Instagram.validators.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentFacade commentFacade;
    @Autowired
    private ResponseErrorValidator responseErrorValidator;


    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment (@Valid @RequestBody CommentDTO commentDTO,
                                                 @PathVariable("postId") String postId,
                                                 BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors  =responseErrorValidator.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return  errors;

        Comment comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal );
        CommentDTO createdComment = commentFacade.commentToCommentDTO(comment);


        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToPost(@PathVariable ("postId") String postId){
        List<CommentDTO> commentDTOList = commentService. getAllCommentsForPost(Long.parseLong(postId))
                .stream().map(commentFacade::commentToCommentDTO).collect(Collectors.toList());


        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("{commentId}/delete")
    public  ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId){
        commentService.deleteComment(Long.parseLong(commentId));

        return new ResponseEntity<>(new MessageResponse("Post was DELETED"),HttpStatus.OK);

    }



}
