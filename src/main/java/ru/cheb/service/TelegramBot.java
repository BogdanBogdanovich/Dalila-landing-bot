package ru.cheb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.cheb.config.BotConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Value("${reg.chat.id}") private String regChatId;


    private final String START_COMMAND = "/start";
    private final String EVENT_COMMAND = "/event";
    private final String PAYMENT_COMMAND = "/payment";
    private final String BUTTON_NUM = "buttonNum";
    private final String BUTTON_ENTER = "buttonEnter";
    private final String BUTTON_CONS = "buttonCons";
    private final String BUTTON_CHOOSE = "buttonChoose";
    private final String START_TEXT = """
            \uD83D\uDCABПриветствую тебя в моем удивительном пространстве! \uD83D\uDCAB
            
            Меня зовут Далила Григорьева, и я рада, что ты здесь! Я занимаюсь предпринимательской деятельностью с подросткового возраста и специализируюсь на помогающих профессиях.
            
            Ты только что попала в уникальный бот, где тебя ждет много полезного и интересного!
            
            \uD83C\uDF81 Сначала мы отправим тебе информативный PDF файл, который предоставит ценные советы и рекомендации для жизни.
            
            \uD83D\uDCCC Далее ты получишь ссылки на все наши ресурсы и контакты, чтобы всегда оставаться на связи и не пропускать важные обновления.
            
            \uD83D\uDCB8 Затем выйдет перечень услуг, за которыми вы в любое время можете обратиться ко мне
            
            \uD83C\uDFA5И вскоре тебя ждет видео с дополнительной полезной информацией, которая вдохновит и наполнит энергией!
            
            Благодарю тебя, что ты с нами!\s
            Давай вместе исследовать мир возможностей и самосовершенствования!""";
    private final String PRICE_TEXT = "Прайс-лист \uD83D\uDCDD \n\n 1. Нумерологический разбор - 990 р. \n 2. Вход в S.p.l.e.t.n.i.c.a woman club - 1490 р. \n 3. Консультация педагога-психолога 45 минут - 3000 р. \n 4. Купить платье - 5990 р. \n\nПосле выбора категории будет выведена более подробная информация!";
    private final String INFO_TEXT = "Полезный материал о планировании, пользе и основных способах: \uD83D\uDCCC \n\nСоц.сети:\n[YouTube](https://youtube.com/@grigorevadalila?si=j0ZULHwGR8uDHJSG) \n[Инстаграм](https://www.instagram.com/grigorevadalila?igsh=cXE3N2FsejJyaGR6&utm_source=qr) \n[Инстаграм магазина](https://www.instagram.com/s.p.l.e.t.n.i.c.a?igsh=MWt0NDJhNnlpcjN1eA==) \n[Телеграм канал](https://t.me/+wTAtYZleoMdiMzli)";
    private final String NUM_TEXT = """
            Сфера личность:
            - это сфера отражает развитие личности и её основных талантов, возможность их раскрытия миру. К последнему относится и то, какие именно навыки необходимо приобрести человеку в этой жизни. Личные минусы и плюсы личности.
            
            Так как сфера личности и её энергии действуют на человека уже с рождения,
            то для родителей важно с раннего детства обратить внимание на сильные стороны ребёнка, его воспитание, не допускать фатальных ошибок и направлять ребёнка в нужное русло, согласно его энергиям.
            
            Рассмотрим какими талантами и врождёнными качествами наделён человек
            с рождения, какие ошибки могут быть допущены родителями при воспитании
            и как их избежать, а также посмотрим какие секции и кружки идеально подходят под конкретного ребёнка в соответствии с арканами в его сфере личности.
            
            С какими трудностями могут столкнуться родители, какие ошибки допускать и как этого избежать и сгладить острые углы?
            
            Сфера духовность:
            - подробно поговорим о нашем втором, духовном треугольнике. Он отвечает за наши стремления к духовности и наши проработки.
            
            Сфера денег:
            - рассмотрим все арканы вашего денежного треугольника, по ним мы можем увидеть профессии, которые подходят вам по данным энергиям, а также то, что блокирует и увеличивает ваш финансовый поток.
            
            Сфера отношений:
            - какой ты в отношениях?
            - какой партнер тебе подходит?
            - подходит ли человек для бизнес партнерства?
            - какие недостатки и преимущества имеете в отношениях
            
            Сфера здоровья:
            - сфера здоровья связывает вашу Душу с физическим телом. Она отвечает за ваши ошибки в сфере здоровья и отношения к своему телу. Это про «Хочу сильное тело, но мне так лень ходить в зал», «Почему я болею этим вообще?»
            
            ------
            
            Нужно сделать репост этого сообщения, тем самым ответив на него, в ответе напишите:\s
            дату рождения и сферу - здоровье, личность, духовность, деньги, отношения\s
            
            И отправьте сообщение, после чего заявка будет отправлена администратору ✅""";

    private final String ENTER_TEXT = """
            S.p.l.e.t.n.i.c.a woman club
            Март - месяц здоровья
            Лекции:
            - 4 прямых эфира с нутрициологом\s
            - Сушка с нутрициологом\s
            - 4 прямого эфира Тренировки танцевальные\s
            - 4 прямого эфира тренировки на пресс, ягодицы, ноги
            - Рецепты на 30 дней завтрак-обед-ужин\s
            - Лекция о правильном питание\s
            - Лекция о проверке внутреннего здоровья через анализы
            - Лекция полезных привычек для здоровья\s
            - Лекция о сне
            - Лекция о правильном распорядке дня. В какое время дня, какой деятельностью заниматься\s
            
            Общение:
            - Каждый понедельник - презентации о себе и своих услугах\s
            - Каждую среду - звездный час для экспертов\s
            - Каждый четверг - обмен и продажа ненужных вещей
            - Каждую субботу - лайкТайм взаимные подписки, лайки и комментарии
            
            ------
            
            Нужно сделать репост этого сообщения, тем самым ответив на него, в ответе напишите, что хотите получить доступ в клуб. После чего будет отправлена заявка администратору и он вышлет вам ссылку на телеграмм канал ✅""";

    private final String CONS_TEXT = """
            Нужно сделать репост этого сообщения, тем самым ответив на него, в ответе напишите: свой запрос и ник в телеграмм ✍\uD83C\uDFFC\s
            
            Отправьте сообщение, после чего заявка будет отправлена администратору ✅""";

    private final String CHOOSE_TEXT = """
            Выберите подходящее платье.
            
            ------
            
            Нужно сделать репост этого сообщения, тем самым ответив на него, в ответе напишите: название платья, размер, адрес пункта СДЭК, номер телефона и ФИО ✍\uD83C\uDFFC\s
            
            Отправьте сообщение, после чего заявка будет отправлена администратору ✅""";

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            // получаем текстовое сообщение
            String messageText = update.getMessage().getText();
            User user = update.getMessage().getFrom();

            switch (messageText) {
                case START_COMMAND:
                    logger.log(Level.INFO, log(START_COMMAND));
                    registerPayment(String.format("В бот выполнен вход, пользователь @%s, фамилия и имя = %s %s, userId = %s", user.getUserName(), user.getLastName(), user.getFirstName(), user.getId()));
                    this.createPhotoAndMessage(update.getMessage(), START_TEXT, "img.png");
                    this.createDocumentWithKeyboardAndMessage(update.getMessage(), INFO_TEXT);
                    this.createMessageWithKeyboard(update.getMessage(), PRICE_TEXT, priceKeyboard());
                    this.createVideo(update.getMessage());
                    break;

                default:
                    if (update.getMessage().getReplyToMessage() != null &&
                            update.getMessage().getReplyToMessage().hasText()) {
                        String replyText = update.getMessage().getReplyToMessage().getText();
                        switch (replyText) {
                            case NUM_TEXT, ENTER_TEXT, CONS_TEXT, CHOOSE_TEXT -> {
                                String text = String.format("Сообщение от пользователя @%s, фамилия и имя = %s %s, userId = %s \n\n %s", user.getUserName(), user.getLastName(), user.getFirstName(), user.getId(), messageText);
                                registerPayment(text);

                                this.createMessageWithKeyboard(update.getMessage(), "Заявка зарегистрирована, данные переданы администратору, спасибо, что воспользовались ботом! \uD83D\uDE4F\uD83C\uDFFC \n\nМожете выбрать ещё что-нибудь!", priceKeyboard());

                            }
                            default -> {
                                this.createMessage(update.getMessage(), "Вы ответили не на то сообщение.");
                            }
                        }
                    }


            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Message message = update.getCallbackQuery().getMessage();
            switch (data) {
                case BUTTON_NUM:
                    logger.log(Level.INFO, log(BUTTON_NUM));
                    createMessage(message, NUM_TEXT);
                    break;

                case BUTTON_ENTER:
                    logger.log(Level.INFO, log(BUTTON_ENTER));
                    createMessage(message, ENTER_TEXT);
                    break;

                case BUTTON_CONS:
                    logger.log(Level.INFO, log(BUTTON_CONS));
                    createMessage(message, CONS_TEXT);
                    break;

                case BUTTON_CHOOSE:
                    logger.log(Level.INFO, log(BUTTON_CHOOSE));
                    createPhoto(message);
                    createMessage(message, CHOOSE_TEXT);
                    break;
                default:
                    logger.log(Level.INFO, log("unknown command"));
                    createMessage(update.getCallbackQuery().getMessage(), "К сожалению не получается распознать команду");
            }
        }
    }

    private void registerPayment(String text) {
        // создаём сообщение
        SendMessage sendMessage = SendMessage.builder()
                .chatId(regChatId)
                .parseMode(ParseMode.MARKDOWN)
                .text(text)
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.log(Level.SEVERE, "error while sending message", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод создания сообщения, для дальнейшей отправки пользователю
     * @param message полученное сообщение
     * @param text текст, который хотим отправить
     */
    private void createMessage(Message message, String text) {

        // создаём сообщение
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId().toString())
                .parseMode(ParseMode.MARKDOWN)
                .text(text)
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.log(Level.SEVERE, "error while sending message", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод создания сообщения с клавиатурой, для дальнейшей отправки пользователю
     * @param message полученное сообщение
     * @param text текст, который хотим отправить
     * @param keyboard клавиатура
     */
    private void createMessageWithKeyboard(Message message, String text, InlineKeyboardMarkup keyboard) {

        // создаём сообщение
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId().toString())
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(keyboard)
                .text(text)
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.log(Level.SEVERE, "error while sending message", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void createPhoto(Message message) {

        // Создаем список медиафайлов
        List<InputMedia> mediaList = new ArrayList<>();

        // Добавляем фотографии в список
        InputMediaPhoto photo1 = new InputMediaPhoto();
        photo1.setMedia(new File("photo_2025-02-28_20-32-01.jpg"), "photo1.jpg");
        mediaList.add(photo1);

        InputMediaPhoto photo2 = new InputMediaPhoto();
        photo2.setMedia(new File("photo_2025-02-28_20-32-02.jpg"), "photo2.jpg");
        mediaList.add(photo2);

        InputMediaPhoto photo3 = new InputMediaPhoto();
        photo3.setMedia(new File("photo_2025-02-28_20-32-03.jpg"), "photo3.jpg");
        mediaList.add(photo3);

        InputMediaPhoto photo4 = new InputMediaPhoto();
        photo4.setMedia(new File("photo_2025-02-28_20-32-04.jpg"), "photo4.jpg");
        mediaList.add(photo4);

        // Создаем объект SendMediaGroup
        SendMediaGroup mediaGroup = new SendMediaGroup();
        mediaGroup.setChatId(message.getChatId().toString());
        mediaGroup.setMedias(mediaList);

        try {
            execute(mediaGroup);
        } catch (TelegramApiException e) {
            logger.log(Level.SEVERE, "error while sending message", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createDocumentWithKeyboardAndMessage(Message message, String text) {


        SendDocument sendDocument = new SendDocument();
        try {
            InputFile inputFile = new InputFile("BQACAgIAAxkDAAIDN2fCOBqrnW5oAhp9UP-twAaMDTOHAAKoZgACfwwZSpi7QAtBB1p7NgQ");
            sendDocument = SendDocument.builder()
                    .chatId(message.getChatId().toString())
                    .document(inputFile)
                    .caption(text)
                    .parseMode(ParseMode.MARKDOWN)
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "photo not found", e);
        }

        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            logger.log(Level.SEVERE, "error while sending message", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод создания сообщения и фото, для дальнейшей отправки пользователю
     * @param message полученное сообщение
     * @param text текст, который хотим отправить
     */
    private void createPhotoAndMessage(Message message, String text, String photoUrl) {
        File image = new File(photoUrl);

        SendPhoto sendPhoto = null;
        if (image != null) {
            try {
                InputFile inputFile = new InputFile(image);
                sendPhoto = SendPhoto.builder()
                        .chatId(message.getChatId().toString())
                        .photo(inputFile)
                        .caption(text)
                        .parseMode(ParseMode.MARKDOWN)
                        .build();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "photo not found", e);
            }

        }

        SendMessage sendMessage = null;
        if (sendPhoto == null) {
            // создаём сообщение
            sendMessage = SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .parseMode(ParseMode.MARKDOWN)
                    .text(text)
                    .build();
        }

        try {
            if (sendPhoto != null)
                execute(sendPhoto);
            else
                execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.log(Level.SEVERE, "error while sending message", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createVideo(Message message) {

        // Создаем объект SendVideo
        SendVideo video = new SendVideo();
        video.setChatId(message.getChatId().toString());

        //File image = new File("C:\\Users\\admin\\IdeaProjects\\dalila-landing-bot\\src\\main\\java\\ru\\cheb\\service\\video_2025-02-28_21-07-17.mp4");
        InputFile inputFile = new InputFile("BAACAgIAAxkBAAIB1WfCB1kZfGfFn0MeAsDFXWL6kkVkAAIwcAAC0icRStNYpRqmWweDNgQ");
        video.setVideo(inputFile);

        try {
            execute(video);
        } catch (TelegramApiException e) {
            logger.log(Level.SEVERE, "error while sending message", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    /**
//     * Метод для создания клавиатуры при приветствии
//     * @return клавиатура с "да/нет"
//     */
//    private InlineKeyboardMarkup needEventKeyboard() {
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
//        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
//
//        buttonYes.setText("Да");
//        buttonYes.setCallbackData(BUTTON_YES);
//
//        buttonNo.setText("Нет");
//        buttonNo.setCallbackData(BUTTON_INFO);
//
//        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
//        rowButtons.add(buttonYes);
//        rowButtons.add(buttonNo);
//
//        List<List<InlineKeyboardButton>> rowsList = new ArrayList<>();
//        rowsList.add(rowButtons);
//
//        inlineKeyboardMarkup.setKeyboard(rowsList);
//        return inlineKeyboardMarkup;
//    }

    /**
     * Метод для создания клавиатуры стоимости
     */
    private InlineKeyboardMarkup priceKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton numButton = new InlineKeyboardButton();
        InlineKeyboardButton enterButton = new InlineKeyboardButton();
        InlineKeyboardButton consButton = new InlineKeyboardButton();
        InlineKeyboardButton chooseButton = new InlineKeyboardButton();

        numButton.setText("Нумерологический разбор");
        numButton.setCallbackData(BUTTON_NUM);

        enterButton.setText("Вход в S.p.l.e.t.n.i.c.a woman.club");
        enterButton.setCallbackData(BUTTON_ENTER);

        consButton.setText("Консультация педагога-психолога");
        consButton.setCallbackData(BUTTON_CONS);

        chooseButton.setText("Купить платье");
        chooseButton.setCallbackData(BUTTON_CHOOSE);

        List<InlineKeyboardButton> rowNumButtons = new ArrayList<>();
        rowNumButtons.add(numButton);

        List<InlineKeyboardButton> rowEnterButtons = new ArrayList<>();
        rowEnterButtons.add(enterButton);

        List<InlineKeyboardButton> rowConsButtons = new ArrayList<>();
        rowConsButtons.add(consButton);

        List<InlineKeyboardButton> rowChooseButtons = new ArrayList<>();
        rowChooseButtons.add(chooseButton);


        List<List<InlineKeyboardButton>> rowsList = new ArrayList<>();
        rowsList.add(rowNumButtons);
        rowsList.add(rowEnterButtons);
        rowsList.add(rowConsButtons);
        rowsList.add(rowChooseButtons);

        inlineKeyboardMarkup.setKeyboard(rowsList);
        return inlineKeyboardMarkup;
    }

    private String log(String action) {
        return String.format("user select '%s' action", action);
    }
}
