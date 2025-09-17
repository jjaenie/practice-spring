package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemoService {

    // 1. 생성자 주입 <-- 생성자가 하나만 있을때는 @Autowired 생략 가능
    private final MemoRepository memoRepository; //final은 바로 초기화를 하거나 생성자를 통해 초기화 해야함

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

//    //2. 메서드로 주입
//    private final MemoRepository memoRepository;
//
//    @Autowired
//    public MemoService(MemoRepository memoRepository) {
//        this.memoRepository = memoRepository;
//    }

//    //3. 필드로 주입 <-- 추천은 안함. 생성자 주입 추천
//    @Autowired
//    private MemoRepository memoRepository;

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        // DB 조회
        return memoRepository.findAllByOrderByModifiedAtDesc().stream().map(MemoResponseDto::new).toList();
    }


    @Transactional
    public Long updateMemo(Long id, MemoRequestDto requestDto) {

        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);
        // memo 내용 수정
        memo.update(requestDto);
        return id;
    }

    public Long deleteMemo(Long id) {

        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);
        // memo 삭제
        memoRepository.delete(memo);

        return id;

    }

    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );
    }


}
