package me.ubi.game;

public enum Event {
    // added
    DOUBLE_DICE("Двойные кубики", "Все игроки кидают два кубика вместо одного"),
    // to do
    EZ_MODE("EZ MODE", "Кубик на проверку успеха грабежа можно перебросить один раз."),
    HARD_MODE("HARD MODE", "При проверке успеха грабежа, если игрок проваливает, то отдаёт все свои плавающие очки тому, кого он пытался ограбить. Если игрок преуспевает, то получает в два раза больше очков"),
    SUDDEN_DEATH("Внезапная Смерть", "Если игроки выкидывают критический провал (1) на любом броске, они теряют треть/половину (на взгляд ведущего) своих закреплённых очков"),
    THE_PURGE("Судная Ночь", "В этот раунд, игроки могут только грабить банк друг друга. Если игрок отказывается грабить, они теряют треть своего банка, а очки делятся между игроками с округлением в большую сторону. (например, 10 очков потеряны - три игрока получают по 4 очка!)"),
    SAVE_THE_PRESIDENT("Спасти Президента", "Выбирается игрок, у кого больше всего очков (если два игрока имеют одинаковое количество очков - выбирается один из них), а остальные игроки делятся на две группы, слева направо чередуясь (первый - 1 группа, второй - 2 группа, третий - 1 группа). Первая группа \"Воры\" (каждый игрок) кидает к8 на попытку грабежа банка самого богатого игрока. Вторая группа \"Защитники\" (каждый игрок) так же кидает к8. Сумма бросков группы складывается. Если \"Воры\" выбрасывают больше \"Защитников\", то они успешно крадут половину очков из банка президента и делят между собой (округляются в большую сторону). Если \"Защитники\" выбрасывают больше \"Воров\", то президент сохраняет свои очки, а защитники делят сохранённые деньги между собой (округляется в меньшую сторону)."),
    SKILLCHECK("Скиллчек!", "Перед началом раунда, все игроки должны бросить к10. Они имеют возможность добавить полученный результат к любому броску один раз."),
    TEAMS("Команды", "Игроки разбиваются на пары. (Если кто-то остался без пары, он играет как обычно, но получает Вдохновение на весь раунд) Они совершают любые броски вместе; при проверке успеха грабежа, выбирается наибольший результат из двух бросков. На момент ивента, их очки складываются. По истечению ивента полученные/потерянные за раунд очки делятся между двумя поровну с округлением в меньшую сторону."),
    DRAGONS_AND_CASINO("Казино и драконы", "Вместо к8 на проверке получения очков и грабежа, все игроки бросают к20. Работает с предметами, но не влияет на проверку успеха грабежа");

    private final String name;
    private final String description;

    Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

}
