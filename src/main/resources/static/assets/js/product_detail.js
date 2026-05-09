/**
 * /src/main/resources/static/assets/js/product_detail.js
 */

// ====================================================
// 화면 UI 관련 구현
// ====================================================

/**
 * 상품 상세 정보 탭 이벤트 핸들러
 * 탭 클릭 시 해당 탭을 활성화하고 다른 탭들을 비활성화함
 */
document.querySelectorAll(".tab-header").forEach((v, i) => {
    v.addEventListener("click", (e) => {
        // 모든 탭 헤더에서 active 클래스 제거
        const tabHeaders = document.querySelectorAll('.tab-header');
        tabHeaders.forEach(header => header.classList.remove('active'));

        // 모든 탭 패인에서 active 클래스 제거
        const tabPanes = document.querySelectorAll('.tab-pane');
        tabPanes.forEach(pane => pane.classList.remove('active'));

        // 클릭된 탭 헤더에 active 클래스 추가
        e.currentTarget.classList.add('active');

        // 클릭된 탭의 data-target 속성 가져오기
        const tabName = e.currentTarget.dataset.target;

        // 해당 탭 패인에 active 클래스 추가하여 표시
        document.getElementById(`${tabName}-tab`).classList.add('active');
    });
});

// ====================================================
// 상품 옵션 조합 관리 관련 구현
// ====================================================

// HTML에서 정의된 상품 정보 변수를 잘 로드하고 있는지 확인하기 위해 로그로 출력
console.group("상품 정보");
console.log(productData);
console.groupEnd();

// 현재 선택된 옵션 조합을 생성하기 위한 빈 객체
let selectedCombinations = [];

/**
 * 상품 옵션 선택 이벤트 핸들러
 * 옵션 선택 시 조합을 생성하고 선택된 조합 목록에 추가
 */
document.querySelectorAll(".option-value").forEach((v, i) => {
    v.addEventListener("click", (e) => {
        /** 1) 클릭된 옵션 요소 */
        const current = e.currentTarget;

        /** 2) 선택된 자신을 기준으로 부모 태그에 속해있는 같은 종류의 요소들을 취득 */
        // --> 같은 종류의 옵션을 의미
        const family = current.parentElement.querySelectorAll('.option-value');
        // --> 같은 종류의 요소들에서 active 클래스 제거
        family.forEach(el => el.classList.remove('selected'));
        // --> 클릭된 옵션에 active 클래스 추가
        current.classList.add('selected');

        /** 3) 옵션 그룹 단위로 반복처리 --> 선택된 데이터 수집 */
        // 옵션 그룹 단위 객체
        const optionGroups = document.querySelectorAll('.option-group');
        // 선택된 항목을 저장할 객체
        const optionChoice = {};

        // 각 옵션 그룹(컬러, 사이즈 등)을 순회
        optionGroups.forEach((vv, ii) => {
            // 각 그룹별로 선택된 항목 가져오기
            const selectedOption = vv.querySelector('.option-value.selected');

            // 선택된 항목의 옵션번호,옵션명,옵션값 가져오기
            if (selectedOption) {
                const optionName = selectedOption.getAttribute('data-option-name');
                const optionValue = selectedOption.innerHTML;
                optionChoice[optionName] = optionValue;
            }
        });

        //console.log(optionChoice);

        // 선택된 옵션 정보의 길이가 전체 옵션 그룹과 같지 않다면 처리 중단
        if (Object.keys(optionChoice).length !== optionGroups.length) {
            return;
        }

        /** 4) 선택된 옵션 정보의 길이와 전체 옵션 그룹 길이가 같다면 선택된 조합에 대한 등록 처리 */
        // --> 선택된 옵션으로 등록하기 위해 수량과 가격 정보 추가

        // 가격은 productData에서 discount가 있다면 할인율을 적용하고, 그렇지 않다면 원래 가격을 사용
        let optionPrice = productData.price;

        if (productData.discount > 0) {
            optionPrice -= parseInt(productData.price * productData.discount / 100)
        }

        optionChoice.price = optionPrice; // 가격 정보 추가
        optionChoice.quantity = 1; // 기본 수량 1
        //console.log(optionChoice);


        /** 5) 선택된 조합을 배열에 추가 */
        // selectedCombinations에 price와 quantity를 제외한 현재 선택된 옵션 조합이 있는지 확인
        const existingCombinationIndex = selectedCombinations.findIndex(combination => {
            const keys = Object.keys(optionChoice);
            // 배열의 모든 원소에 대해서 순환하면서, 모든 콜백이 true를 리턴할 때만 최종 결과가 true
            const sameOption = keys.every(k => {
                return k === 'price' || k === 'quantity' || combination[k] === optionChoice[k];
            });

            // 모든 옵션이 동일한 조합이 존재한다면 true, 그렇지 않다면 false
            // --> sameOption이 true를 리턴하는 순간에 대한 selectedCombinations의 인덱스가 existingCombinationIndex에 저장됨
            return sameOption;
        });

        // 이미 존재하는 조합이 있다면 해당 조합의 수량을 증가시키고, 그렇지 않다면 새로운 조합으로 추가
        if (existingCombinationIndex !== -1) {
            // 기존 조합의 수량을 증가
            selectedCombinations[existingCombinationIndex].quantity += 1;
        } else {
            // 신규로 선택된 조합이므로 고유 ID를 생성하여 옵션 정보에 추가
            const newCombination = {
                id: Date.now(), // 고유 ID 생성 (현재 시간 기반)
                ...optionChoice // 기존 옵션 정보 병합
            }
            
            // 새로운 조합으로 추가
            selectedCombinations.push(newCombination);
        }

        console.log(selectedCombinations);

        /** 6) 모든 선택 사항 초기화 */
        optionGroups.forEach((vv, ii) => {
            const selectedOption = vv.querySelector('.option-value.selected');
            if (selectedOption) {
                selectedOption.classList.remove('selected');
            }
        });

        /** 7) 선택된 조합 정보를 조합하여 화면 UI에 반영 */
        updateCombinationDisplay();
    });
});

/**
 * 상품 옵션 조합 정보를 화면에 표시하는 함수
 */
const updateCombinationDisplay = () => {
    const combinationList = document.querySelector('#combinationList');
    combinationList.innerHTML = '';

    // 선택된 항목이 없다면 조합 리스트를 숨기고 처리 중단
    if (selectedCombinations.length === 0) {
        combinationList.style.display = 'none';
        return;
    }

    // 선택된 조합이 있다면 조합 리스트를 표시
    combinationList.style.display = 'block';

    // 옵션 정보만큼 순회
    selectedCombinations.forEach((vv, ii) => {
        // 옵션정보(vv) 예시 --> {id: 1234, 컬러: '데님', 사이즈: '235', price: 33600, quantity: 1}
        console.log(vv);

        // 옵션 정보 표시
        const combinationOptions = document.createElement('div');
        combinationOptions.className = 'combination-options';

        // 하나의 옵션 정보에 대해 모든 키를 배열로 추출 후 순회
        Object.keys(vv).forEach(k => {
            if (k !== 'id' &&k !== 'price' && k !== 'quantity') {
                const optionItem = document.createElement('span');
                optionItem.className = 'combination-option';
                optionItem.textContent = `${k}: ${vv[k]}`;
                combinationOptions.appendChild(optionItem);
            }
        });

        // 가격 정보 표시
        const combinationPrice = document.createElement('div');
        combinationPrice.className = 'combination-price';
        combinationPrice.textContent = `₩${(vv.price * vv.quantity).toLocaleString()}`;

        // 조합 정보 영역 생성후 옵션 정보와 가격정보 추가
        const combinationInfo = document.createElement('div');
        combinationInfo.className = 'combination-info';
        combinationInfo.appendChild(combinationOptions);
        combinationInfo.appendChild(combinationPrice);

        // 수량 감소 버튼
        const decreaseBtn = document.createElement('button');
        decreaseBtn.type = 'button';
        decreaseBtn.className = 'combination-quantity-btn';
        decreaseBtn.textContent = '-';
        decreaseBtn.addEventListener('click', () => changeCombinationQuantity(vv.id, -1));

        // 수량 입력 필드
        const quantityInput = document.createElement('input');
        quantityInput.type = 'number';
        quantityInput.className = 'combination-quantity-input';
        quantityInput.value = vv.quantity;
        quantityInput.min = '1';
        quantityInput.addEventListener('change', (e) => setCombinationQuantity(vv.id, e.target.value));

        // 수량 증가 버튼
        const increaseBtn = document.createElement('button');
        increaseBtn.type = 'button';
        increaseBtn.className = 'combination-quantity-btn';
        increaseBtn.textContent = '+';
        increaseBtn.addEventListener('click', () => changeCombinationQuantity(vv.id, 1));

        // 수량 조절 영역 생성
        const combinationQuantity = document.createElement('div');
        combinationQuantity.className = 'combination-quantity';

        // 수량 조절 영역에 버튼들 추가
        combinationQuantity.appendChild(decreaseBtn);
        combinationQuantity.appendChild(quantityInput);
        combinationQuantity.appendChild(increaseBtn);

        // 삭제 버튼 생성
        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.className = 'combination-remove';
        removeBtn.textContent = '삭제';
        removeBtn.addEventListener('click', () => removeCombination(vv.id));

        // 메인 컨테이너 생성
        const combinationItem = document.createElement('div');
        combinationItem.className = 'combination-item';

        // 메인 컨테이너에 모든 요소 추가
        combinationItem.appendChild(combinationInfo);
        combinationItem.appendChild(combinationQuantity);
        combinationItem.appendChild(removeBtn);

        // 조합 리스트에 추가
        combinationList.appendChild(combinationItem);
    });

    // 총 가격 계산 및 표시
    // 모든 조합의 총 수량 계산
    const totalQuantity = selectedCombinations.reduce((sum, combo) => sum + combo.quantity, 0);

    // 모든 조합의 총 가격 계산
    const totalPrice = selectedCombinations.reduce((sum, combo) => sum + (combo.price * combo.quantity), 0);

    // DOM 요소 참조
    const totalQuantityElement = document.getElementById('totalQuantity');
    const totalPriceElement = document.getElementById('totalPrice');
    const totalSummary = document.getElementById('totalSummary');

    if (totalQuantity > 0) {
        // 계산된 값을 화면에 표시
        totalQuantityElement.textContent = `총 ${totalQuantity}개`;
        totalPriceElement.textContent = `₩${totalPrice.toLocaleString()}`;
        totalSummary.style.display = 'block'; // 요약 정보 표시
    } else {
        // 선택된 상품이 없으면 요약 정보 숨김
        totalSummary.style.display = 'none';
    }
};

/**
 * 특정 조합의 수량을 변경 (+1 또는 -1)
 * @param {number} combinationId - 조합 ID
 * @param {number} delta - 변경량 (+1 또는 -1)
 */
const changeCombinationQuantity = (combinationId, delta) => {
    const combination = selectedCombinations.find(combo => combo.id === combinationId);
    if (combination) {
        // 최소 수량은 1개로 제한
        combination.quantity = Math.max(1, combination.quantity + delta);
        updateCombinationDisplay();
    }
};

/**
 * 특정 조합을 목록에서 제거
 * @param {number} combinationId - 제거할 조합의 고유 ID
 */
function removeCombination(combinationId) {
    // 해당 ID를 제외한 조합들로 배열 재구성
    selectedCombinations = selectedCombinations.filter(combo => combo.id !== combinationId);
    // 화면에 출력되는 옵션 목록 갱신
    updateCombinationDisplay();
};

// ====================================================
// 장바구니에 담기
// ====================================================
document.querySelector(".btn-cart").addEventListener("click", async (e) => {
    e.preventDefault();

    // 선택된 조합이 없으면 처리 중단
    if (selectedCombinations.length === 0) {
        alert("장바구니에 담을 상품이 없습니다. 옵션을 선택해주세요.");
        return;
    }

    // 요청에 필요한 FormData 생성
    const formData = new FormData();
    formData.append('productId', productData.id);       // 상품 ID

    // 상품 옵션 하나를 `|`로 조합하여 formData에 추가
    selectedCombinations.forEach((v, i) => {
        let optionValue = "";
        Object.keys(v).forEach((vv, ii) => {
            optionValue += `${vv}:${v[vv]}|`;
        });
        // 마지막 파이프(|) 제거
        optionValue = optionValue.slice(0, -1);
        console.log(`${i}번째 옵션: ${optionValue}`);
        formData.append(`options`, optionValue);
    });

    try {
        await fetchHelper.post('/api/cart/add', formData);
    } catch (e) {
        console.error('장바구니 추가 중 오류 발생:', e);
        alert('장바구니에 상품을 추가하는 중 오류가 발생했습니다. 다시 시도해주세요.');
        return;
    }

    // 성공 메시지
    if (confirm('장바구니에 상품이 추가되었습니다. 장바구니로 이동하시겠습니까?')) {
        // 장바구니 페이지로 이동
        // window.location.href = '/cart';
        alert("이 예제는 여기까지 구현입니다. 장바구니 페이지로 이동하는 기능은 실제 프로젝트에서 구현해주세요.");
    }
});