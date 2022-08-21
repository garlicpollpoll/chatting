package com.capston.chatting.controller.item;

import com.capston.chatting.dto.Item.ItemUploadDto;
import com.capston.chatting.entity.Item;
import com.capston.chatting.entity.Member;
import com.capston.chatting.repository.ItemRepository;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.ItemService;
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

    @GetMapping("/item_list")
    public String itemList(Model model) {
        List<Item> findAll = itemRepository.findAll();

        model.addAttribute("items", findAll);

        return "item_list";
    }

    @GetMapping("/item_detail/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {
        Item findItem = itemRepository.findById(itemId).orElse(null);

        model.addAttribute("item", findItem);

        return "item_detail";
    }

    @GetMapping("/upload")
    public String upload() {
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
