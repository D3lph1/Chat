/**
 * The file with main application logic.
 */

import './theme'
import $ from 'jquery'
import SockJS from 'sockjs-client'
import Stomp from 'stomp-websocket'

// CSRF-token will be useful for async requests.
const token = $("meta[name='_csrf']").attr('content');
const header = $("meta[name='_csrf_header']").attr('content');

/**
 * Hide displayed errors on login/signup pages.
 */
function hideError() {
    $('#alert-container').css('display', 'none');
}

/**
 * Display errors on login/signup pages.
 *
 * @param {string[]} errors
 */
function displayFirstError(errors) {
    if (errors.length === 0) {
        return;
    }

    $('#alert-container > div > p').text(errors[0]);
    $('#alert-container').css('display', 'block');
}

$('#signup-btn').click(() => {
    $.ajax('/signup', {
        method: 'POST',
        beforeSend: request => {
            hideError();
            $('#signup-btn').attr('disabled', true);

            request.setRequestHeader(header, token);
        },
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({
            firstName: $('#signup-first-name').val(),
            lastName: $('#signup-last-name').val(),
            email: $('#signup-email').val(),
            password: $('#signup-password').val(),
            passwordConfirmation: $('#signup-password-confirmation').val()
        })
    })
        .always(() => {
            $('#signup-btn').attr('disabled', false);
        })
        .done(response => {
            if (response.status === 'success') {
                // We add hash "#successful_signup" to the url to display successfully signup
                // alert on the login page.
                window.location.replace('/login#successful_signup');
            } else {
                displayFirstError(response.errors);
            }
        })
        .fail(response => {
            displayFirstError(response.responseJSON.errors);
        });
});

$(document).ready(() => {
    // Display successfully login alert.
    if (window.location.pathname === '/login' && window.location.hash === '#successful_signup') {
        $('#successful-signup-alert').css('display', 'block');
    }

    if (window.location.pathname === '/') {
        connect();
    }
});

$('#login-btn').click(() => {
    $.ajax('/login', {
        method: 'POST',
        beforeSend: request => {
            $('#login-btn').attr('disabled', true);

            request.setRequestHeader(header, token);
        },
        data: {
            email: $('#login-email').val(),
            password: $('#login-password').val(),
        }
    })
        .always(() => {
            $('#login-btn').attr('disabled', false);
        })
        .done(response => {
            if (response.status === 'success') {
                window.location.replace('/');
            } else {
                displayFirstError(response.errors);
            }
        })
        .fail(response => {
            displayFirstError(response.responseJSON.errors);
        });
});

let stompClient = null;

/**
 * The function establishes websocket connection with the server and in case of
 * success subscribes to events. It allows to receive chat updates in real
 * time.
 */
function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, () => {
        selectUser($($('.avatar')[0]));

        stompClient.subscribe('/broker/connected', data => {
            const user = JSON.parse(data.body);
            if (user.email !== $('#my-email').text()) {
                const alreadyExisting = $(`.avatar[data-email="${user.email}"]`);
                if (alreadyExisting.length !== 0) {
                    return;
                }
                const avatar = $('#avatar-template').clone();
                avatar.removeAttr('id');
                avatar.addClass('avatar');
                avatar.attr('src', user.avatarUrl);
                avatar.attr('title', user.firstName + ' ' + user.lastName + ' [' + user.email + ']');
                avatar.attr('data-email', user.email);
                avatar.attr('data-first-name', user.firstName);
                avatar.attr('data-last-name', user.lastName);
                avatar.css('display', 'inline-block');

                $('#avatars').append(avatar);
                selectUser($($('.avatar')[0]));
            }
        });

        stompClient.subscribe('/broker/disconnected', data => {
            const user = JSON.parse(data.body);
            if (user.email !== $('#my-email').text()) {
                const alreadyExisting = $(`.avatar[data-email="${user.email}"]`);
                if (alreadyExisting.length === 0) {
                    return;
                }
                alreadyExisting[0].remove();
            }
        });

        stompClient.subscribe('/user/queue/send', response => {
            response = JSON.parse(response.body);
            const data = response.body.data;
            const receiver = $('#receiver').text();
            // If a message was sent from this account. Cause of this message even can be
            // other device or browser.
            if (data.sender === $('#my-email').text()) {
                addSentMessage(data.content);
            } else {
                addReceivedMessage(data.content, data.sender, $(`.avatar[data-email="${receiver}"]`));
            }
        });
    });
}

/**
 * The function changes current selected user to a specified. It clear #messages container
 * and after then make request to load chat history for a new specified user.
 *
 * @param {$} selectedUserEl
 */
function selectUser(selectedUserEl) {
    const receiverEmail = selectedUserEl.attr('data-email');
    $('#receiver').text(receiverEmail);
    if (selectedUserEl.hasClass('unread')) {
        selectedUserEl.removeClass('unread');
    }
    selectedUserEl.addClass('selected-user');
    $('.avatar').each((_, each) => {
        if (each !== selectedUserEl.get(0)) {
            const jEach = $(each);
            if (jEach.hasClass('selected-user')) {
                jEach.removeClass('selected-user');
            }
        }
    });

    $.ajax('/chat', {
        method: 'GET',
        beforeSend: request => {
            request.setRequestHeader(header, token);
        },
        data: {
            receiverEmail
        }
    })
        .done(response => {
            if (response.status === 'success') {
                // Clear all displayed (except template) content of #messages div.
                $('.message').remove();

                response.data.history.forEach(historyItem => {
                    if (historyItem.fromCurrent) {
                        addSentMessage(historyItem.content);
                    } else {
                        addReceivedMessage(historyItem.content, selectedUserEl.attr('data-email'), selectedUserEl);
                    }
                });
            }
        });
}

/**
 * Add message to the #messages container. This message will be displayed as message
 * which you sent.
 *
 * @param {string} messageContent
 */
function addSentMessage(messageContent) {
    const messages = $('#messages');

    const message = $('#message-template').clone();
    message.removeAttr('id');
    message.addClass('message');

    let content;
    const avatar = message.find('.message-avatar');
    const contentContainer = message.find('.message-content-container');
    contentContainer.detach();

    avatar.attr('src', $('#my-avatar').text());
    message.removeClass('guest uk-flex-left uk-card-small');
    message.addClass('me uk-flex-right uk-text-right');
    message.prepend(contentContainer);
    contentContainer.find('.message-user').text(
        $('#my-first-name').text() + ' ' + $('#my-last-name').text()
    );
    content = message.find('.message-content');
    content.removeClass('uk-card-default');
    content.addClass('uk-card-primary');

    const lines = messageContent.split(/\r?\n/);
    lines.forEach(line => {
        content.append(
            `<p class="uk-margin-remove">${line}</p>`
        );
    });

    message.css('display', '');

    messages.append(message);
    messages.scrollTop(messages[0].scrollHeight);
}

/**
 * Add message to the #messages container. This message will be displayed as message
 * which you received.
 *
 * @param {string} messageContent
 * @param {string} sender A sender identifier
 * @param {$} selectedUserEl An user from which was sent this message
 */
function addReceivedMessage(messageContent, sender, selectedUserEl) {
    // If received message corresponds to an other user. In this case we need change
    // image style of the sender icon.
    if (sender !== selectedUserEl.attr('data-email')) {
        const user = $(`.avatar[data-email="${sender}"]`);
        user.removeClass('selected-user');
        user.addClass('unread');

        return;
    }

    const messages = $('#messages');

    const message = $('#message-template').clone();
    message.removeAttr('id');
    message.addClass('message');

    let content;
    const avatar = message.find('.message-avatar');
    const contentContainer = message.find('.message-content-container');
    contentContainer.detach();

    avatar.attr('src', selectedUserEl.attr('src'));
    message.removeClass('me uk-flex-right uk-text-right uk-card-primary');
    message.addClass('guest uk-flex-left uk-card-small');
    message.append(contentContainer);
    contentContainer.find('.message-user').text(
        selectedUserEl.attr('data-first-name') + ' ' + selectedUserEl.attr('data-last-name')
    );
    content = message.find('.message-content');
    content.removeClass('uk-card-primary');
    content.addClass('uk-card-default');

    const lines = messageContent.split(/\r?\n/);
    lines.forEach(line => {
        content.append(
            `<p class="uk-margin-remove">${line}</p>`
        );
    });

    message.css('display', '');

    messages.append(message);
    messages.scrollTop(messages[0].scrollHeight);
}

$('#send').click(send);

/**
 * Sends message from the input to the server.
 */
function send() {
    const input = $('#input');
    const content = input.val().trim();
    if (content === '') {
        return;
    }

    // Send message to the server. Notice that after successful response completion
    // we don't set this message as sent. We make it only when will happen
    // '/user/queue/send' event.
    $.ajax('/send', {
        method: 'POST',
        beforeSend: request => {
            request.setRequestHeader(header, token);
        },
        data: {
            receiverEmail: $('#receiver').text(),
            content
        }
    })
        .done(response => {
            if (response.status === 'success') {
                input.val('');
            } else if (response.status === 'user_offline') {
                alert($('#user-offline-alert').text());
            }
        })
        .fail(response => {
            if (response.responseJSON.status === 'user_offline') {
                alert($('#user-offline-alert').text());
            }
        });
}

/**
 * Add custom enter/ctrl+enter input text area behavior. As a result message will be
 * sent by pressing Enter, whereas Ctrl + Enter combination add a new line.
 */
$('#input').keydown(event => {
    // Enter pressed
    if (event.keyCode === 13) {
        // And ctrl pressed
        if (!event.ctrlKey) {
            event.preventDefault();
            send();
        }
    }
});

$('body').on('click', '.avatar', function (event) {
    selectUser($(event.target));
});
