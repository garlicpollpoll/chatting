package com.capston.chatting.controller;

import com.capston.chatting.dto.ItemUploadDto;
import com.capston.chatting.entity.Item;
import com.capston.chatting.entity.Member;
import com.capston.chatting.repository.ItemRepository;
import com.capston.chatting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadPost(@ModelAttribute("item")ItemUploadDto dto, RedirectAttributes redirectAttributes) {
        Member member = new Member("경석");
        memberRepository.save(member);

        Item item = new Item(dto.getItemName(), Integer.parseInt(dto.getPrice()), dto.getComment());
        itemRepository.save(item);

        redirectAttributes.addAttribute("id", item.getId());

        return "redirect:/item_detail/{id}";
    }

    @GetMapping("/item_detail/{id}")
    public String itemDetail(@PathVariable("id") Long id, Model model) {
        Item findItem = itemRepository.findById(id).orElse(null);

        model.addAttribute("item", findItem);

        return "item_detail";
    }
}
