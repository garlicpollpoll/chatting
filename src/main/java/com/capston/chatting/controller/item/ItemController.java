package com.capston.chatting.controller.item;

import com.capston.chatting.dto.Item.ItemUploadDto;
import com.capston.chatting.entity.Item;
import com.capston.chatting.entity.Member;
import com.capston.chatting.repository.ItemRepository;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.item.ItemService;
import com.capston.chatting.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * 현재 등록된 상품들을 보여주는 페이지로 연결되는 컨트롤러
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/item_list")
    public String itemList(Model model, HttpSession session) {
        List<Item> findAll = itemRepository.findAll();

        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            Member findMember = memberRepository.findMemberByLoginId(loginId);
            memberService.updateDate(findMember);
        }


        model.addAttribute("items", findAll);

        return "item_list";
    }

    /**
     * itemId 에 따라 상품의 정보를 보여주는 페이지로 이동되는 컨트롤러
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping("/item_detail/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {
        Item findItem = itemRepository.findById(itemId).orElse(null);

        model.addAttribute("item", findItem);

        return "item_detail";
    }

    /**
     * 상품 업로드 페이지로 이동되는 컨트롤러
     * @param session
     * @return
     */
    @GetMapping("/upload")
    public String upload(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            Member findMember = memberRepository.findMemberByLoginId(loginId);
            memberService.updateDate(findMember);
        }

        return "item_upload";
    }

    @PostMapping("/upload")
    public String uploadPost(@ModelAttribute("item")ItemUploadDto dto, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberRepository.findMemberByLoginId(loginId);

        itemService.save(dto, findMember);

        return "redirect:/item_list";
    }
}
