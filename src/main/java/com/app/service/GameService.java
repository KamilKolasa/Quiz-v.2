package com.app.service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Game;
import com.app.model.dto.GameDto;
import com.app.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public GameDto addGame(GameDto game) {
        if (game == null) {
            throw new MyException(ExceptionCode.SERVICE, "ADD GAME - GAME IS NULL");
        }
        Game g = ModelMapper.fromGameDtoToGame(game);
        return ModelMapper.fromGameToGameDto(gameRepository.save(g));
    }
}
