package com.capston.chatting.service.item;

import com.capston.chatting.dto.Item.ItemUploadDto;
import com.capston.chatting.entity.Item;
import com.capston.chatting.entity.Member;
import com.capston.chatting.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public void save(ItemUploadDto dto, Member member) {
        Item item = new Item(dto.getItemName(), Integer.parseInt(dto.getPrice()), dto.getComment(), member);

        itemRepository.save(item);
    }
}
