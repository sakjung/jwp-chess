// draggable setting

const $squares = document.querySelectorAll("td[class=chessboard]");

for (let i = 0; i < $squares.length; i++) {
    $squares[i].addEventListener("dragstart", dragstart_handler)
    $squares[i].addEventListener("dragover", dragover_handler)
    $squares[i].addEventListener("drop", drop_handler)
}

function dragstart_handler(event) {
    event.dataTransfer.setData("text/plain", event.target.parentNode.id);
    event.dataTransfer.dropEffect = "move";
}

function dragover_handler(event) {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
}

function drop_handler(event) {
    event.preventDefault()
    const source = event.dataTransfer.getData("text/plain");
    const target = event.target.parentNode.id;

    const moveCommand = "move " + source + " " + target;
    sendMoveRequest(moveCommand)
}

// move, save, exit setting

const $move = document.querySelector('input[class="move"]')

$move.addEventListener('keyup', movePiece);

function movePiece(event) {
    const moveCommand = event.target.value;
    if (event.key === "Enter" && moveCommand !== "") {
        event.target.value = "";
        const trimmedMoveCommand = moveCommand
            .replace(/\s+/g, ' ')
            .trim()
        sendMoveRequest(trimmedMoveCommand)
    }
}

function sendMoveRequest(trimmedMoveCommand) {
    const http = new XMLHttpRequest();
    const url = window.location + "/move";

    http.open('POST', url);
    http.setRequestHeader('Content-type', 'text/plain');
    http.onreadystatechange = function () {
        const sourcePosition = trimmedMoveCommand.split(" ")[1]
        const targetPosition = trimmedMoveCommand.split(" ")[2]

        if (http.readyState === XMLHttpRequest.DONE) {
            if (http.status === 200) {
                replaceComponents(http.responseText, sourcePosition, targetPosition)
            } else {
                console.log(trimmedMoveCommand)
                alert(sourcePosition + "에서" + targetPosition + "으로 움직일 수 없습니다!")
            }
        }
    }

    http.send(trimmedMoveCommand);
}

function replaceComponents(dom, sourcePosition, targetPosition) {
    let parser = new DOMParser();
    let xmlDoc = parser.parseFromString(dom, "text/html");

    let source = xmlDoc.querySelector("#" + sourcePosition);
    let target = xmlDoc.querySelector("#" + targetPosition);
    let turn = xmlDoc.querySelector("#turn")
    let score = xmlDoc.querySelector("#score")

    document.querySelector("#" + sourcePosition).innerHTML = source.innerHTML
    document.querySelector("#" + targetPosition).innerHTML = target.innerHTML
    document.querySelector("#turn").innerHTML = turn.innerHTML
    document.querySelector("#score").innerHTML = score.innerHTML

    const outcome = xmlDoc.querySelector("#outcome").textContent
    if (outcome !== "") {
        alert("black: " + outcome[0] + " / white : " + outcome[1])
        window.location.replace("/")
    }
}